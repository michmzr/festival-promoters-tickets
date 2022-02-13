package eu.cybershu.service.impl;

import eu.cybershu.service.PromotorService;
import eu.cybershu.domain.Promotor;
import eu.cybershu.repository.PromotorRepository;
import eu.cybershu.service.dto.PromotorDTO;
import eu.cybershu.service.mapper.PromotorMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Promotor}.
 */
@Service
@Transactional
public class PromotorServiceImpl implements PromotorService {

    private final Logger log = LoggerFactory.getLogger(PromotorServiceImpl.class);

    private final PromotorRepository promotorRepository;

    private final PromotorMapper promotorMapper;

    public PromotorServiceImpl(PromotorRepository promotorRepository, PromotorMapper promotorMapper) {
        this.promotorRepository = promotorRepository;
        this.promotorMapper = promotorMapper;
    }

    @Override
    public PromotorDTO save(PromotorDTO promotorDTO) {
        log.debug("Request to save Promotor : {}", promotorDTO);
        Promotor promotor = promotorMapper.toEntity(promotorDTO);
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
