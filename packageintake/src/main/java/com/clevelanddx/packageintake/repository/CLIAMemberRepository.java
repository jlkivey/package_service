package com.clevelanddx.packageintake.repository;

import com.clevelanddx.packageintake.model.CLIAMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CLIAMemberRepository extends JpaRepository<CLIAMember, Long> {
    Optional<CLIAMember> findByUserId(String userId);
    List<CLIAMember> findByLastUpdateUser(String lastUpdateUser);
    boolean existsByUserId(String userId);
    
    @Query("SELECT m FROM CLIAMember m WHERE LOWER(m.userId) LIKE LOWER(CONCAT('%', :searchTerm, '%')) ORDER BY m.userId")
    List<CLIAMember> searchByUserId(@Param("searchTerm") String searchTerm);

    @Query(value = "SELECT * FROM CLIA_Member WHERE LOWER(User_ID) LIKE LOWER(CONCAT('%', :searchTerm, '%')) ORDER BY User_ID", nativeQuery = true)
    List<CLIAMember> searchMemberByPartial(@Param("searchTerm") String searchTerm);
}
