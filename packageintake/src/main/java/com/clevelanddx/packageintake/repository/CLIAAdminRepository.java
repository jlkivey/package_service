package com.clevelanddx.packageintake.repository;

import com.clevelanddx.packageintake.model.CLIAAdmin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CLIAAdminRepository extends JpaRepository<CLIAAdmin, Long> {
    Optional<CLIAAdmin> findByUserId(String userId);
    List<CLIAAdmin> findByLastUpdateUser(String lastUpdateUser);
    boolean existsByUserId(String userId);
    
    @Query("SELECT a FROM CLIAAdmin a WHERE LOWER(a.userId) LIKE LOWER(CONCAT('%', :searchTerm, '%')) ORDER BY a.userId")
    List<CLIAAdmin> searchByUserId(@Param("searchTerm") String searchTerm);

    @Query(value = "SELECT * FROM CLIA_Admin WHERE LOWER(User_ID) LIKE LOWER(CONCAT('%', :searchTerm, '%')) ORDER BY User_ID", nativeQuery = true)
    List<CLIAAdmin> searchAdminByPartial(@Param("searchTerm") String searchTerm);
}
