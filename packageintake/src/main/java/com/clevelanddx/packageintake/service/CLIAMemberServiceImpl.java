package com.clevelanddx.packageintake.service;

import com.clevelanddx.packageintake.model.CLIAMember;
import com.clevelanddx.packageintake.repository.CLIAMemberRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CLIAMemberServiceImpl implements CLIAMemberService {
    
    private final CLIAMemberRepository repository;
    
    @Autowired
    public CLIAMemberServiceImpl(CLIAMemberRepository repository) {
        this.repository = repository;
    }
    
    @Override
    public CLIAMember create(CLIAMember member) {
        member.setLastUpdateDatetime(LocalDateTime.now());
        return repository.save(member);
    }

    @Override
    public CLIAMember update(Long id, CLIAMember member) {
        Optional<CLIAMember> existingMember = repository.findById(id);
        if (existingMember.isPresent()) {
            CLIAMember updatedMember = existingMember.get();
            updatedMember.setUserId(member.getUserId());
            updatedMember.setLastUpdateDatetime(LocalDateTime.now());
            updatedMember.setLastUpdateUser(member.getLastUpdateUser());
            return repository.save(updatedMember);
        }
        throw new EntityNotFoundException("CLIA Member not found with id: " + id);
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("CLIA Member not found with id: " + id);
        }
        repository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CLIAMember> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CLIAMember> findAll() {
        return repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CLIAMember> findByUserId(String userId) {
        return repository.findByUserId(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CLIAMember> findByLastUpdateUser(String lastUpdateUser) {
        return repository.findByLastUpdateUser(lastUpdateUser);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByUserId(String userId) {
        return repository.existsByUserId(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CLIAMember> searchMembers(String searchTerm) {
        return repository.searchByUserId(searchTerm);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CLIAMember> searchMemberByPartial(String searchTerm) {
        return repository.searchMemberByPartial(searchTerm);
    }
}
