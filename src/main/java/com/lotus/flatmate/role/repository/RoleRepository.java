package com.lotus.flatmate.role.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lotus.flatmate.role.entity.Role;
import com.lotus.flatmate.role.entity.RoleName;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long>{

	Optional<Role> findByName(RoleName roleUser);

}
