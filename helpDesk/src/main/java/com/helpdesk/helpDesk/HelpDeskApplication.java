package com.helpdesk.helpDesk;

import com.helpdesk.helpDesk.entities.PermissionEntity;
import com.helpdesk.helpDesk.entities.RoleEntity;
import com.helpdesk.helpDesk.entities.Usuario;
import com.helpdesk.helpDesk.entities.enums.Rol;
import com.helpdesk.helpDesk.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Set;

@SpringBootApplication
@RequiredArgsConstructor
public class HelpDeskApplication {

	private final PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(HelpDeskApplication.class, args);
	}


//	@Bean
//	CommandLineRunner init(UsuarioRepository userRepository){
//		return args -> {
//			/* PASO UNO: CREAR PERMISOS  */
//			PermissionEntity createPermission = PermissionEntity.builder()
//					.name("CREATE")
//					.build();
//
//			PermissionEntity readPermission = PermissionEntity.builder()
//					.name("READ")
//					.build();
//
//			PermissionEntity updatePermission = PermissionEntity.builder()
//					.name("UPDATE")
//					.build();
//
//			PermissionEntity patchPermission = PermissionEntity.builder()
//					.name("PATCH")
//					.build();
//
//			PermissionEntity deletePermission = PermissionEntity.builder()
//					.name("DELETE")
//					.build();
//
//
//
//			/*PASO DOS: CREAR ROLES y asignarles permisos*/
//			RoleEntity roleAdmin = RoleEntity.builder()
//					.roleEnum(Rol.ADMIN)
//					.permissionList(Set.of(createPermission, readPermission, updatePermission, deletePermission, patchPermission))
//					.build();
//
//			RoleEntity roleCliente = RoleEntity.builder()
//					.roleEnum(Rol.CLIENTE)
//					.permissionList(Set.of(createPermission, readPermission))
//					.build();
//
//			RoleEntity roleTecnico = RoleEntity.builder()
//					.roleEnum(Rol.TECNICO)
//					.permissionList(Set.of(createPermission, readPermission, updatePermission, deletePermission, patchPermission))
//					.build();
//
//
//			/*PASO TRES: CREAR USUARIOS*/
//			Usuario userAdmin = Usuario.builder()
//					.username("admin")
//					.password(passwordEncoder.encode("2345"))
//					.email("gilbertojgutierrezm1997@gmail.com")
//					.isEnabled(true)
//					.accountNoExpired(true)
//					.accountNoLocked(true)
//					.credentialNoExpired(true)
//					.roles(Set.of(roleAdmin))
//					.build();
//
//			Usuario userCliente = Usuario.builder()
//					.username("cliente")
//					.password(passwordEncoder.encode("2345"))
//					.email("gilbertojgm78@gmail.com")
//					.isEnabled(true)
//					.accountNoExpired(true)
//					.accountNoLocked(true)
//					.credentialNoExpired(true)
//					.roles(Set.of(roleCliente))
//					.build();
//
//			Usuario userTecnico = Usuario.builder()
//					.username("tecnico")
//					.password(passwordEncoder.encode("2345"))
//					.email("gilbertojgm78@gmail.com")
//					.isEnabled(true)
//					.accountNoExpired(true)
//					.accountNoLocked(true)
//					.credentialNoExpired(true)
//					.roles(Set.of(roleTecnico))
//					.build();
//
//
//			//guardamos todos los usuarios en la bd
//			userRepository.saveAll(List.of(userAdmin, userCliente, userTecnico));
//		};
//	}
}
