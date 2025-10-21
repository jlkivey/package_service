package com.clevelanddx.packageintake.service;

import com.clevelanddx.packageintake.model.CLIAAdmin;
import java.util.List;
import java.util.Optional;

public interface CLIAAdminService {
    CLIAAdmin create(CLIAAdmin admin);
    CLIAAdmin update(Long id, CLIAAdmin admin);
    void delete(Long id);
    Optional<CLIAAdmin> findById(Long id);
    List<CLIAAdmin> findAll();
    Optional<CLIAAdmin> findByUserId(String userId);
    List<CLIAAdmin> findByLastUpdateUser(String lastUpdateUser);
    boolean existsByUserId(String userId);
    List<CLIAAdmin> searchAdmins(String searchTerm);
    List<CLIAAdmin> searchAdminByPartial(String searchTerm);
}
