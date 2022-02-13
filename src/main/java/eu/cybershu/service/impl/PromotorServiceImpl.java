package eu.cybershu.service.impl;

import eu.cybershu.domain.PromoCode;
import eu.cybershu.repository.PromoCodeRepository;
import eu.cybershu.service.PromotorService;
import eu.cybershu.domain.Promotor;
import eu.cybershu.repository.PromotorRepository;
import eu.cybershu.service.dto.PromotorCreateDTO;
import eu.cybershu.service.dto.PromotorDTO;
import eu.cybershu.service.mapper.PromotorMapper;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.HashSet;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link Promotor}.
 */
@Slf4j
@Service
@Transactional
public class PromotorServiceImpl implements PromotorService {
    private final PromotorRepository promotorRepository;
    private final PromotorMapper promotorMapper;

    private final PromoCodeRepository promoCodeRepository;

    public PromotorServiceImpl(PromotorRepository promotorRepository, PromotorMapper promotorMapper, PromoCodeRepository promoCodeRepository) {
        this.promotorRepository = promotorRepository;
        this.promotorMapper = promotorMapper;
        this.promoCodeRepository = promoCodeRepository;
    }

    @Override
    public PromotorDTO save(PromotorDTO promotorDTO) {
        log.debug("Request to save Promotor : {}", promotorDTO);
        Promotor promotor = promotorMapper.toEntity(promotorDTO);
        promotor = promotorRepository.save(promotor);
        return promotorMapper.toDto(promotor);
    }

    @Override
    public PromotorDTO create(PromotorCreateDTO promotorDTO) {
        log.debug("Request to create Promotor : {}", promotorDTO);
        Promotor promotor = new Promotor();
        promotor.setName(promotorDTO.getName());
        promotor.setLastName(promotorDTO.getLastName());
        promotor.setEmail(promotorDTO.getEmail());
        promotor.setEnabled(true);
        promotor.setCreatedAt(Instant.now());
        promotor.setPhoneNumber(promotor.getPhoneNumber());

        promotor.setPromoCodes(
        promotorDTO.getCodes()
            .stream()
            .map(code -> {
                PromoCode promoCode = new PromoCode();
                promoCode.setCode(code);
                promoCode.setCreatedAt(Instant.now());
                promoCode.setEnabled(true);
                return promoCode;
            }).collect(Collectors.toSet()));

        log.debug("Creating promotor {}", promotor);
        promotor = promotorRepository.save(promotor);
        return promotorMapper.toDto(promotor);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PromotorDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Promotors");
        return promotorRepository.findAll(pageable)
            .map(promotorMapper::toDto);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<PromotorDTO> findOne(Long id) {
        log.debug("Request to get Promotor : {}", id);
        return promotorRepository.findById(id)
            .map(promotorMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Promotor : {}", id);
        promotorRepository.deleteById(id);
    }
}
