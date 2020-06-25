package cubes.main;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.User.UserBuilder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private DataSource myDataSource;
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		
		UserBuilder users = User.withDefaultPasswordEncoder();
		
		auth.inMemoryAuthentication()
		.withUser(users.username("username").password("password").roles("employee").disabled(true));
			
		
		auth.jdbcAuthentication().dataSource(myDataSource);
	}

	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http.authorizeRequests()
		.antMatchers("/").hasAnyRole("admin, employee")
		.antMatchers("/administration/category-list").hasRole("admin")
		.antMatchers("/administration/category-form").hasRole("admin")
		.antMatchers("/administration/tag-form").hasRole("admin")
		.antMatchers("/administration/tag-list").hasRole("admin")
		.antMatchers("/administration/employee-form").hasRole("admin")
		.antMatchers("/administration/employee-list").hasRole("admin")
		.antMatchers("/administration/users-list").hasRole("admin")

		.antMatchers("/administration/**").hasAnyRole("employee, admin")
		
		.and().formLogin()
		.loginPage("/loginPage")
		.loginProcessingUrl("/authenticateTheUser").permitAll();
	}
	
}
