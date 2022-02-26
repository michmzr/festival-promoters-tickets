package eu.cybershu.service.impl;

import eu.cybershu.domain.PromoCode;
import eu.cybershu.repository.PromoCodeRepository;
import eu.cybershu.service.PromoCodeService;
import eu.cybershu.service.dto.PromoCodeDTO;
import eu.cybershu.service.mapper.PromoCodeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link PromoCode}.
 */
@Service
@Transactional
public class PromoCodeServiceImpl implements PromoCodeService {

    private final Logger log = LoggerFactory.getLogger(PromoCodeServiceImpl.class);

    private final PromoCodeRepository promoCodeRepository;

    private final PromoCodeMapper promoCodeMapper;

    public PromoCodeServiceImpl(PromoCodeRepository promoCodeRepository, PromoCodeMapper promoCodeMapper) {
        this.promoCodeRepository = promoCodeRepository;
        this.promoCodeMapper = promoCodeMapper;
    }

    @Override
    public PromoCodeDTO save(PromoCodeDTO promoCodeDTO) {
        log.debug("Request to save PromoCode : {}", promoCodeDTO);
        PromoCode promoCode = promoCodeMapper.toEntity(promoCodeDTO);
        promoCode = promoCodeRepository.save(promoCode);
        return promoCodeMapper.toDto(promoCode);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PromoCodeDTO> findAll() {
        log.debug("Request to get all PromoCodes");
        return promoCodeRepository.findAll().stream()
            .map(promoCodeMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PromoCodeDTO> findOne(Long id) {
        log.debug("Request to get PromoCode : {}", id);
        return promoCodeRepository.findById(id)
            .map(promoCodeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PromoCodeDTO> findOne(String code) {
        log.debug("Request to get PromoCode : code={}", code);
        return promoCodeRepository.findByCodeIgnoreCase(code)
            .map(promoCodeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete PromoCode : {}", id);
        promoCodeRepository.deleteById(id);
    }
}
