package pl.km.exercise291.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> getRoleByName(String name);
}
