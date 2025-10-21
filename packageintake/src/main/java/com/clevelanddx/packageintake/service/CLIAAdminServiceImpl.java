package com.clevelanddx.packageintake.service;

import com.clevelanddx.packageintake.model.CLIAAdmin;
import com.clevelanddx.packageintake.repository.CLIAAdminRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CLIAAdminServiceImpl implements CLIAAdminService {
    
    private final CLIAAdminRepository repository;
    
    @Autowired
    public CLIAAdminServiceImpl(CLIAAdminRepository repository) {
        this.repository = repository;
    }
    
    @Override
    public CLIAAdmin create(CLIAAdmin admin) {
        admin.setLastUpdateDatetime(LocalDateTime.now());
        return repository.save(admin);
    }

    @Override
    public CLIAAdmin update(Long id, CLIAAdmin admin) {
        Optional<CLIAAdmin> existingAdmin = repository.findById(id);
        if (existingAdmin.isPresent()) {
            CLIAAdmin updatedAdmin = existingAdmin.get();
            updatedAdmin.setUserId(admin.getUserId());
            updatedAdmin.setLastUpdateDatetime(LocalDateTime.now());
            updatedAdmin.setLastUpdateUser(admin.getLastUpdateUser());
            return repository.save(updatedAdmin);
        }
        throw new EntityNotFoundException("CLIA Admin not found with id: " + id);
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("CLIA Admin not found with id: " + id);
        }
        repository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CLIAAdmin> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CLIAAdmin> findAll() {
        return repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CLIAAdmin> findByUserId(String userId) {
        return repository.findByUserId(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CLIAAdmin> findByLastUpdateUser(String lastUpdateUser) {
        return repository.findByLastUpdateUser(lastUpdateUser);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByUserId(String userId) {
        return repository.existsByUserId(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CLIAAdmin> searchAdmins(String searchTerm) {
        return repository.searchByUserId(searchTerm);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CLIAAdmin> searchAdminByPartial(String searchTerm) {
        return repository.searchAdminByPartial(searchTerm);
    }
}
