package eu.cybershu.service;

import com.google.zxing.WriterException;
import eu.cybershu.service.dto.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.io.IOException;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
public class OrderImportJob {
    private final GuestService guestService;
    private final PromoCodeService promoCodeService;
    private final TicketTypeService ticketTypeService;
    private final TicketService ticketService;

    private final Validator validator;

    public OrderImportJob(Validator validator, GuestService guestService,
                          PromoCodeService promoCodeService,
                          TicketTypeService ticketTypeService, TicketService ticketService) {
        this.validator = validator;
        this.guestService = guestService;
        this.promoCodeService = promoCodeService;
        this.ticketTypeService = ticketTypeService;
        this.ticketService = ticketService;
    }

    public OrderImportResult processRecord(OrderRecord orderRecord) {
        log.info("Importing record {}", orderRecord);

        OrderImportResult importResult = new OrderImportResult();
        importResult.setOrderRecord(orderRecord);

        try {
            //validation
            ValidationResult validationResult = validate(orderRecord);
            log.debug("record validation result: {}", validationResult);

            importResult.setValidation(validationResult);

            if (validationResult.valid()) {
                GuestDTO guestDTO = findGuestByEmailOrCreateNew(orderRecord);
                TicketDTO ticketDTO = createTicket(guestDTO, orderRecord, importResult);

                importResult.setGuestDTO(guestDTO);
                importResult.setTicketDTO(ticketDTO);
                importResult.setProcessed(true);
            } else {
                log.info("Record not passed validation: {}", validationResult.getMessages());
            }
        } catch (Exception e) {
            log.error("Catched exception '{}'", e.getMessage(), e);
            importResult.setProcessed(false);
            importResult.addMessage(e.getMessage());
        }

        return importResult;
    }

    private TicketDTO createTicket(GuestDTO guestDTO, OrderRecord orderRecord, OrderImportResult importResult) throws IOException, WriterException {
        TicketCreateDTO ticketCreateDTO = new TicketCreateDTO();
        ticketCreateDTO.setGuestId(guestDTO.getId());
        ticketCreateDTO.setTicketPrice(orderRecord.getPrice());

        //PromoCode
        String promoCode = orderRecord.getCouponCode();
        var promoCodeOpt = promoCodeService.findOne(promoCode);
        log.debug("Recognised promo code record {}", promoCodeOpt);
        if (promoCodeOpt.isPresent()) {
            ticketCreateDTO.setPromoCodeId(promoCodeOpt.get().getId());
            ticketCreateDTO.setPromotorId(promoCodeOpt.get().getPromotorId());
        } else {
            importResult.addMessage(
                String.format("Not found '%s' code in system", promoCode)
            );
        }

        //Ticket Type
        var ticketTypeOpt = ticketTypeService.findOneByProductId(orderRecord.getProductId());
        log.debug("ticket type: {}", ticketTypeOpt);
        if (ticketTypeOpt.isPresent()) {
            ticketCreateDTO.setTicketTypeId(ticketTypeOpt.get().getId());
        } else {
            throw new IllegalArgumentException(
                String.format(
                    "Not recognised ticket type with defined product id = '%s'. Create ticket type in panel.",
                    orderRecord.getProductId()));
        }

        TicketDTO ticketDTO = ticketService.create(ticketCreateDTO);
        log.info("Created ticket {}", ticketDTO);

        return ticketDTO;
    }

    private GuestDTO findGuestByEmailOrCreateNew(OrderRecord orderRecord) throws IOException, WriterException {
        log.info("Find guest  by email {} or create", orderRecord.getGuestEmail());

        Optional<GuestDTO> guestDTOOpt = guestService.findByEmail(orderRecord.getGuestEmail());
        log.info("Finding result: {}", guestDTOOpt);

        if (guestDTOOpt.isPresent()) {
            return guestDTOOpt.get();
        } else {
            GuestCreateDTO guestCreateDTO = GuestCreateDTO.builder()
                .email(orderRecord.getGuestEmail())
                .name(orderRecord.getGuestName())
                .lastName(orderRecord.getGuestLastName())
                .build();

            GuestDTO guestDTO = guestService.save(guestCreateDTO);
            log.debug("Guest created: {}", guestDTO);

            return guestDTO;
        }
    }

    private ValidationResult validate(OrderRecord orderRecord) {
        var violations = validator.validate(orderRecord);

        if (violations.isEmpty()) {
            return ValidationResult.ok();
        } else {
            ValidationResult validationResult = new ValidationResult();
            validationResult.setFieldNames(
                violations
                    .stream()
                    .map(v -> v.getPropertyPath().toString())
                    .collect(Collectors.toUnmodifiableSet())
            );

            validationResult.setMessages(
                violations.
                    stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.toUnmodifiableSet())
            );

            return validationResult;
        }


    }

}
