package com.clevelanddx.packageintake.service;

import com.clevelanddx.packageintake.model.CLIAMember;
import java.util.List;
import java.util.Optional;

public interface CLIAMemberService {
    CLIAMember create(CLIAMember member);
    CLIAMember update(Long id, CLIAMember member);
    void delete(Long id);
    Optional<CLIAMember> findById(Long id);
    List<CLIAMember> findAll();
    Optional<CLIAMember> findByUserId(String userId);
    List<CLIAMember> findByLastUpdateUser(String lastUpdateUser);
    boolean existsByUserId(String userId);
    List<CLIAMember> searchMembers(String searchTerm);
    List<CLIAMember> searchMemberByPartial(String searchTerm);
}
