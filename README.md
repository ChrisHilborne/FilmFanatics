# Proyecto Final Tokio School

## Introducción

Este documento es la explicación del Proyecto Final del curso de Spring Boot de Tokio School.

La plataforma esta disponible [aquí](https://film-fanaticos.herokuapp.com/) pero como esta alojado en in instance de Heroku, puede tardar unos segundos en cargar.

Todo el código del proyecto completo se encuentra en este [GitHub repository](https://github.com/ChrisHilborne/FilmFanatics/).

Los commits para cada de los tres pasos del proyecto se encuentran aquí:
- [Página Web Thymeleaf con Bootstrap
  ](https://github.com/ChrisHilborne/FilmFanatics/tree/56a745ecc8123bef86ca39947ddd14d3d7db599d)
- [RESTful API con JWT authentication y OpenAPI 2.0 docs](https://github.com/ChrisHilborne/FilmFanatics/tree/3cd934ac5416db14ddbbe46fafd4c48c9e8f889e)
- [Spring Batch Job](https://github.com/ChrisHilborne/FilmFanatics/tree/9f258ab4252977efe1a8cded16d3de2b014ac0a1)

En vez de incluir grabaciones de pantalla, proveeré enlaces al código en el repository de GitHub. También 
Proporcionaré en el propio documento fragmentos de código que requieren más explicación.

## Estructura del Programa

La estructura final del programa es:


## Sumario


## Usuario

El Usuario es la fundación de nuestra app. Casio todos los datos, de `Film`, `Score` y `Review` pertenecen a un `User`. Son los usuarios que añadan los datos del app y, si un `User` se elimina del app - dichos datos también serán eliminados.   

### Objeto de Dominio
[User](https://github.com/ChrisHilborne/FilmFanatics/blob/main/src/main/java/io/chilborne/filmfanatic/domain/User.java)

Como se puede ver, decidí implementar el `interface` de `UserDetails` con el objeto de dominio `User`.

Asi, se puede cast a `User` el objeto `Principal` del `Authentication` que contiene los detalles del session de usuario en el `SecurityContext`. Por ejemplo en el `FilmController` método `FilmInfo`: 

```
@RequestMapping(path = "films/{filmUrl}/score", method = RequestMethod.POST)
  public String filmInfo(@PathVariable("filmUrl") String filmUrl,
                         @ModelAttribute("newScore") @Valid Score score,
                         BindingResult result,
                         Authentication auth) {
    if (result.hasErrors()) {
      return "film";
    }
    score.setUser((User) auth.getPrincipal());
    filmService.addScore(filmUrl, score);

    return "redirect: /films/" + filmUrl;
  }
  ```

Como el único usuario que puede añadir un nuevo `Score` es el que esta ya autenticado, no sea necesario hacer otra llamada al base de datos para añadir lo al nuevo `Score`, sino solo añadir el `User` del `Authentication`. Aun que se puede eliminar llamadas innecesarias al base de datos también con el caching de resultados y los anotaciones de Spring `@EnableCaching`, `@Cacheable` y `@ClearCache`.    

Para implementar el  `UserDetails` interface era necesario `@Override` varios métodos:

```
@Override
public boolean isAccountNonExpired() {
  return true;
}

@Override
public boolean isAccountNonLocked() {
  return true;
}

@Override
public boolean isCredentialsNonExpired() {
  return true;
}

@Override
public boolean isEnabled() {
  return active;
}
```

Para el método de `getAuthorities()` usé una propiedad `Collection<GrantedAuthority> authorities;` con la anotación `@Transient` asi que no fuera guardado en el base de datos, que implicaría otro tabla en dicho base. De esta manera era necesario implementar el método asi:
```
@Override
public Collection<? extends GrantedAuthority> getAuthorities() {
  if (authorities == null) {
    Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
    roles.forEach(role -> grantedAuthorities.add(new SimpleGrantedAuthority(role.getName())));
    authorities = new ArrayList<>(grantedAuthorities);
  }
  return authorities;
}
```
La función es similar de la initialization perezosa de un objeto de tipo singleton. Si la propiedad esta `null`, se crea una colección de autoridades hechas de los `Role` del usuario y se asigna a la propiedad, si no esta null se devuelta. De esta manera solo se necesita cargar las autoridades una vez durante la vida del objeto `User`. Si en algún momento se cambiara los `Role` del usuario, solo sería necesario llamar este mismo método para actualizar las autoridades del usuario.

También se tiene que entender que todos los objetos de dominio que tiene colecciones como miembros, el `User` incluido, esta creados con estas colecciones vaciás y métodos de utilidad para añadir y eliminar objetos de ellas. Por ejemplo, en el `User`:
```
@OneToMany(mappedBy = "user", orphanRemoval = true)
private final Set<Film> films = new HashSet<>();

public void addFilm(Film film) {
  films.add(film);
}

public void removeFilm(Film film) {
  films.remove(film);
}
```
Se hace asi porque el framework ORM (Object Relational Mapping) de `Hibernate` se mantiene sesiones de los objetos, y las colecciones miembros están incluidas en estas sesiones. Si se asigna una nueva colección a la propiedad del objeto `Hibernate` no le va a persistir en el base de datos porque no forma parte de dicha sesión. 

En el código arriba se ve que los `Film` del `User` tienen la anotación de `@OneToMany` con la opción de `orphanRemoval = true`. Asi cuando se elimina el usuario, todos las películas que este ha añadido a la plataforma también serán eliminadas. Es igual para los `Review` y `Score`.


Como el servidor usa el `username` y no el numero de identificación para buscar un `User`, añadí otro index al objeto de dominio: 
```
@Table(indexes = @Index(name = "usrname_index", columnList = "username"))
```   
Asi cuando se busca un `User` con su `username` es más rápido.

### Creación de Usuario

* [index.html](https://github.com/ChrisHilborne/FilmFanatics/blob/main/src/main/resources/templates/index.html)
* [head.html](https://github.com/ChrisHilborne/FilmFanatics/blob/main/src/main/resources/templates/fragments/head.html)
* [header.html](https://github.com/ChrisHilborne/FilmFanatics/blob/main/src/main/resources/templates/fragments/header.html)
* [footer.html](https://github.com/ChrisHilborne/FilmFanatics/blob/main/src/main/resources/templates/fragments/footer.html)
* [UserRepository](https://github.com/ChrisHilborne/FilmFanatics/blob/main/src/main/java/io/chilborne/filmfanatic/repository/UserRepository.java)

Al llegar a la paginá inicial del plataforma de FilmFanatic@s encontramos en el header de la paginá un botón de 'Register' para crear un usuario nuevo.       

En el fichero `header.html` podemos ver que si no hay una usuario autenticado, se muestra los botones para iniciar sesión ('Login') o registrarse ('Register').
```
<div class="d-flex align-items-center" sec:authorize="!isAuthenticated()">
  <a th:href="@{/login}" class="link-primary">Login</a>
  <small class="mx-1" style="color:rgba(209, 204, 192,1.0)" >or</small>
  <a class="btn btn-primary btn-sm" th:href="@{/register}">Register</a>
</div>
```
Al pinchar el botón para registrarse, se llama el método con `@RequestMapping` de GET en el controlador `UserController`. Este método crea un nuevo objeto de tipo `CreateUserDTO` y lo adjunta con el model del `registration.html`. También carga el model con los Role disponible por si caso es un Admin que quiere crear un `User` nuevo.
```
@RequestMapping(path = "/register", method = RequestMethod.GET)
  public String registerUser(Model model) {
    model.addAttribute("createUserDTO", new CreateUserDTO());
    model.addAttribute("roles", roleService.getAllRoles());
    return "registration";
  }
```
#### CreateUserDTO

* [CreateUserDTO](https://github.com/ChrisHilborne/FilmFanatics/blob/main/src/main/java/io/chilborne/filmfanatic/domain/dto/CreateUserDTO.java)
* [PasswordDTO](https://github.com/ChrisHilborne/FilmFanatics/blob/main/src/main/java/io/chilborne/filmfanatic/domain/dto/PasswordDTO.java)

Decidí usar objetos de DTO para la creación y actualización de los usuarios para poder verificar las contraseñas. También solo es necesario incluir los campos necesarios para crear o actualizar un `User` y no todos. 

El `CreateUserDTO` extiende el clase abstracta `PasswordDTO`:
```
public abstract class PasswordDTO {

  @NotBlank(message = "{field.mandatory}")
  @Size(min= 8, message = "{field.password.length}")
  protected String password;
  @NotNull(message = "{field.password.match}")
  protected String confirmPassword;

  public void setPassword(String password) {
    this.password = password;
    confirmPassword();
  }

  public void setConfirmPassword(String confirmPassword) {
    this.confirmPassword = confirmPassword;
    confirmPassword();
  }

  private void confirmPassword() {
    if (this.password == null || this.confirmPassword == null) {
      return;
    }
    else if (!this.password.equals(this.confirmPassword)) {
      this.confirmPassword = null;
    }
  }
}
```
El `PasswordDTO` funciona para verificar que las dos contraseñas que escribe el usuario nuevo son iguales. Si no son, se pone el campo de `confirmPassword` como `null` y eso activa la sistema de validación que esta vinculando al campo con la anotación de `@NotNull(message = "{field.password.match}")`.
Todos los mensajes de validación están inducidos en el fichero `validation-messages.properties` ([GitHub](https://github.com/ChrisHilborne/FilmFanatics/blob/main/src/main/resources/validation-messages.properties)) y configurado por el objeto de `@Configuration` de `ValidationMessageConfig` ([GitHub](https://github.com/ChrisHilborne/FilmFanatics/blob/main/src/main/java/io/chilborne/filmfanatic/config/ValidationMessageConfig.java)).    

#### registration.html
* [registration.html](https://github.com/ChrisHilborne/FilmFanatics/blob/main/src/main/resources/templates/registration.html)

La paginá ya servida lleva el formulario para crear un `User` nuevo.
```
<form th:method="POST" enctype="multipart/form-data" th:object="${createUserDTO}" th:action="@{/register}">
``` 
Este formulario carga el objeto de CreateUserDTO con los datos ingresados por el usuario, y también puede enviar un imagen del usuario si se quiere.
```
<div class="col">
  <label for="userImage" class="labels">Profile Picture</label>
  <input class="form-control" type="file" name="userImage" id="userImage" style="padding: 0.25rem;">
</div>
```
Usé el 'widget' de [bootstrap-datepicker](https://bootstrap-datepicker.readthedocs.io/en/latest/) para seleccionar la fecha de nacimiento del usuario.
```
<div class="col">
  <label for="birthDate" class="labels">Birthdate</label>
  <input type="text" data-provide="datepicker" data-date-format="dd/mm/yyyy" class="form-control datepicker" id="birthDate" autocomplete="off" th:field="*{birthDate}">
</div>
```
Esto require que se carga el `.css` y `.js` en el fichero `head.html` que esta incluida en todos los ficheros de `.html` como fragment.
```
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.9.0/css/bootstrap-datepicker.min.css" integrity="sha512-mSYUmp1HYZDFaVKK//63EcZq4iFWFjxSL+Z3T/aCt4IO9Cejm03q3NKKYN6pFQzY0SBOr8h+eCIAZHPXcpZaNw==" crossorigin="anonymous" referrerpolicy="no-referrer" />
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.9.0/css/bootstrap-datepicker.standalone.min.css" integrity="sha512-TQQ3J4WkE/rwojNFo6OJdyu6G8Xe9z8rMrlF9y7xpFbQfW5g8aSWcygCQ4vqRiJqFsDsE1T6MoAOMJkFXlrI9A==" crossorigin="anonymous" referrerpolicy="no-referrer" />
```
Todos los campos del `CreateUserDTO` que tienen anotaciones de validación también llevan un aviso de error en la paginá de registro si por caso hayan errores en los datos ingresados. Estes avisos solo se muestran si cuando el formulario esta enviado al servidor hay errores. Por ejemplo:
```
<input type="password" placeholder="Repeat Password" th:field="*{confirmPassword}" class="form-control" style="margin-top: 0.2rem;">
<div class="alert alert-warning mb-0 mt-1 py-1" th:if="${#fields.hasErrors('confirmPassword')}" th:errors="*{confirmPassword}"></div>
```  

Si es un Admin que quiere crear un `User` nuevo tambien se muestra la opción de decidir los `Role` del usuario nuevo: 
```
 <div class="col" sec:authorize="hasAuthority('ADMIN')">
    <label for="role">Role</label>
    <select id="role" class="form-select" th:field="*{roles}">
        <option th:each="role : ${roles}" th:value="${role.id}" th:text="${role.name}"></option>
    </select>
</div>
```
#### UserController
* [UserController](https://github.com/ChrisHilborne/FilmFanatics/blob/main/src/main/java/io/chilborne/filmfanatic/controller/thymeleaf/UserController.java)

Al enviar los datos del usuario nuevo al servidor se llama el método POST `registerNewUser()` del `UserController`:
```
@RequestMapping(path = "/register", method = RequestMethod.POST)
  public String registerNewUser(@RequestParam("userImage")MultipartFile imageFile,
                                @ModelAttribute @Valid CreateUserDTO createUserDTO,
                                BindingResult result,
                                Authentication auth)
  {
    if (result.hasErrors()) {
      return "registration";
    }
    User newUser = userService.add(createUserDTO.buildUser());
    if (!imageFile.isEmpty()) {
      userService.saveUserImage(newUser.getUsername(), imageFile);
    }
    if (auth != null &&
      auth.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN"))) {
      return "redirect:/admin/user/" + newUser.getUsername() + "?user=created";
    }
    return "login";
  }
``` 
Este método tiene 6 pasos:
1. Primero se devuelva la paginá si el `BindingResult` tiene errores para que los avisos sean mostrados al usuario.
2. Se crea un objeto de `User` con el método `buildUser()` del `CreateUserDTO` que simplemente mapea los campos del DTO al objeto de dominio.
3. Se llama el método `add(User user)` de `UserService` para guardar el `User` creado en el base de datos.
4. Si el formulario lleva un imagen, se llama el método `saveUserImage(String username, MultipartFile imageFile)` de `UserService` para guardarlo.  
5. Si el autenticado usuario es de tipo Admin, se enviá a la pagina de información sobre el usuario recién creado.
6. Si no es autenticado ni Admin, se devuelve la paginá de iniciar sesión para que el usuario nuevo haga el login. 

#### UserService
* [UserServiceImpl](https://github.com/ChrisHilborne/FilmFanatics/blob/main/src/main.java/java/io/chilborne/filmfanatic/service/implementation/UserServiceImpl.java)

##### add(User)

* [UserRepository](https://github.com/ChrisHilborne/FilmFanatics/blob/main/src/main/java/io/chilborne/filmfanatic/repository/UserRepository.java)

```
@Override
@Transactional
public User add(User user) {
  if (userRepo.findByUsername(user.getUsername()).isPresent()) {
    throw new UsernameAlreadyExistsException("Username not available");
  }
  BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
  user.setPassword(encoder.encode(user.getPassword()));
  user.setCreationDate(LocalDate.now());
  user.setActive(true);
  user.setImage(DEFAULT_PROFILE_IMAGE);

  if (user.getRoles().isEmpty()) {
    user.addRole(roleRepo.findByNameIgnoreCase("USER"));
  }

  return userRepo.save(user);
}
```

El método `add(User user)` tiene
8 pasos:
1. Llama el método `findByUsername(String username)` del repository `UserRepository`
para busca  cualquier `User` con el `username` del objeto nuevo. Si uno ya existe, se lanza un error de tipo `UsernameAlreadyExistsException` ([GitHub](https://github.com/ChrisHilborne/FilmFanatics/blob/main/src/main/java/io/chilborne/filmfanatic/exception/UsernameAlreadyExistsException.java)).
2. Se crea un nuevo instancia del `BCryptPasswordEncoder` para codificar la contraseña del usuario nuevo antes de guardarlo en el base de datos. 
3. Se codificar la contraseña del `User` y se guarda en dicho objeto. 
4. Se establece la fecha de creación del `User`.
5. Se activa el `User`.
6. Se asigna al `User` nuevo el imagen de usuario por defecto, si hay un imagen incluido con el formulario de registro, se actualiza más adelante.
7. Si el `User` no tiene `Role` es porque no se ha creado un 'Admin', asi que hay que asignarlo el `Role` de 'User'.
8. Se guarda el `User` nuevo en el base de datos con una llamada al método `save(User user)` del `UserRepository` y devuelva el `User` ya guardado.

##### saveUserImage(String username, MultipartFile imageFile)

* [StringUtils](https://github.com/ChrisHilborne/FilmFanatics/blob/main/src/main/java/io/chilborne/filmfanatic/util/StringUtil.java)

```
@Override
  @Transactional
  public void saveUserImage(String username, MultipartFile imageFile) {
    User toUpdate = userRepo.findByUsername(username).orElseThrow(UserNotFoundException::new);
    String imageFileName = StringUtil.getUserImageFileName(toUpdate.getId(), imageFile.getContentType());

    if (!toUpdate.getImage().equals(imageFileName)) {
      toUpdate.setImage(imageFileName);
      userRepo.save(toUpdate);
    }
    fileService.saveFile(imageFile, imageFileName);
  }
```

Este método es usado en el caso de la creación de un usuario nuevo, o el caso del cambio del imagen de un usuario existente. 

Tiene 4 pasos:
1) Busca el `User` en el base de datos y si no existe lanza una excepción.
2) Llama el método `getUserImageFileName(long userId, String contentType)` para tener el nombre con que se guarda el imagen.

```
public static String getUserImageFileName(long userId, String contentType) {
  return userId + getFileExtension(contentType);
}

private static String getFileExtension(String contentType) {
  return "." + contentType.split("/")[1];
}
```

El método usa el numero id del usuario por si en caso de que se cambie el `username`, no es necesario cambiar el capo de `image` en el objeto `User`.    

3) Si el campo `image` del `User` es diferente que el `imageFileName` ya devuelto, por ejemplo si es el nombre del imagen que se usa por defecto, se actualiza el campo y guarda el `User` actualizado en el base de datos. 

4) Llama el método `saveFile(MultipartFile file, String fileName)` del `FileService` que el `UserServiceImpl` tiene como dependencia.

#### FileService
* [FileServiceImpl](https://github.com/ChrisHilborne/FilmFanatics/blob/main/src/main/java/io/chilborne/filmfanatic/service/implementation/FileServiceImpl.java)
* [FileServiceConfig](https://github.com/ChrisHilborne/FilmFanatics/blob/main/src/main/java/io/chilborne/filmfanatic/config/FileServiceConfig.java)

Se inyecta al `UserServiceImpl` el `FileService` asi.

```
@Service
public class UserServiceImpl implements UserService {

  private final UserRepository userRepo;
  private final RoleRepository roleRepo;
  private final FileService fileService;

  private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

  public UserServiceImpl(UserRepository userRepo,
                         RoleRepository roleRepo,
                         @Qualifier("user-image-file-service") FileService fileService) {
    this.userRepo = userRepo;
    this.roleRepo = roleRepo;
    this.fileService = fileService;
  }
```

El `Bean` del `FileService` esta cualificada porque existen dos en el programa. Uno para gestionar los archivos de imágenes del usuarios, y otro para los de películas.

Como los dos `FileService` funcionan igual, solo se guardan los archivo en destinos diferentes, es más fácil usar solo una clase que se carga al momento de su creación con el destino diferente. Esto hacemos en la clase de configuración `FileServiceConfig`.

```
@Configuration
public class FileServiceConfig {

  @Value("${images.upload.directory.user}")
  private String userImageUploadDirectory;
  @Value("${images.upload.directory.film}")
  private String filmPosterUploadDirectory;

  @Bean("user-image-file-service")
  public FileService userImageFileService() {
    return new FileServiceImpl(userImageUploadDirectory);
  }

  @Bean("film-poster-file-service")
  public FileService filmPosterFileService() {
    return new FileServiceImpl(filmPosterUploadDirectory);
  }

}
```
Y el constructor de la clase `FileServiceImpl` asi.
```
@Slf4j
public class FileServiceImpl implements FileService {

  private String uploadDirectory;

  public FileServiceImpl(String uploadDirectory) {
    this.uploadDirectory = uploadDirectory;
  }
```
Los destinos están contenidos en la ficha de propiedades `application.properties`.

```
images.upload.directory.user=static/images/users
images.upload.directory.film=static/images/films
```

##### saveFile(MultipartFile file, String fileName)

* [FileUtils](https://github.com/ChrisHilborne/FilmFanatics/blob/main/src/main/java/io/chilborne/filmfanatic/util/FileUtils.java)

```
@Override
public void saveFile(MultipartFile file, String fileName) throws ImageUploadException {
  log.info("Saving file {} as {}", file, fileName);
  try (InputStream fileStream = file.getInputStream()) {
    Path imagePath = FileUtils.getResourcePath(uploadDirectory, fileName);
    if (Files.exists(imagePath)) {
      Files.copy(fileStream, imagePath, StandardCopyOption.REPLACE_EXISTING);
    }
    else {
      Files.write(imagePath, fileStream.readAllBytes(), StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE);
    }
  } catch (IOException e) {
    log.error("Error saving file {}", fileName, e);
    throw new ImageUploadException("Image Upload Failed", e);
  }
}
```
El método para guardar un archivo tiene 5 pasos:
1) Escribe al log para informar del proceso de guardar el archivo. 
2) Dentro de un bucle de 'try-else' se saca un `InputStream` del archivo. Asi que si algo falla, el recurso se cierra automáticamente. 
3) Llama el método estático `getResourcePath(String uploadDirectory, String fileName)` de la clase `FileUtils` para obtener el `Path` donde se va guardar el archivo:
```
private static Path getRootResourcePath() throws IOException {
  return Paths.get(resourceLoader.getResource("classpath:").getURI());
}

public static Path getResourcePath(String directory, String fileName) throws IOException {
  Path path = Paths.get(getRootResourcePath() + File.separator + directory + File.separator + fileName);
  return path;
}
```
4) Si ya hay un archivo guardado a este `Path` se sobrescribe con el archivo nuevo. 
5) Si no existe un archivo a este `Path` todavía, se lo escribe nuevo. 

### Login
* [login.html](https://github.com/ChrisHilborne/FilmFanatics/blob/main/src/main/resources/templates/login.html)

Al pinchar en el link de 'Login' para iniciar sesión el método GET `login(Model model)` del `UserController` devuelve la pagina de `login.html` donde se encuentra el formulario para indicar el username y contraseña del `User`. 

#### WebSecurityConfig 
* [WebSecurityConfig](https://github.com/ChrisHilborne/FilmFanatics/blob/main/src/main/java/io/chilborne/filmfanatic/security/WebSecurityConfig.java)

La clase `WebSecurityConfig` que extiende el `WebSecurityConfigurerAdapter` proporcionado por Spring Security es donde se configura la método de iniciar sesión. 

```
@Override
protected void configure(HttpSecurity http) throws Exception {
  http
    .csrf()
      .disable()
    .authorizeRequests()
      .antMatchers("/").permitAll()
      .antMatchers("/register").permitAll()
      .antMatchers("/login").permitAll()
      .antMatchers("/film/**").permitAll()
      .antMatchers("/films/**").permitAll()
      .antMatchers("/search").permitAll()
      .antMatchers("/h2-console/**").permitAll()
      .antMatchers("/v3/api-docs/**" ,"/swagger-ui/**", "swagger-ui.html").permitAll()
      .antMatchers("/admin/**").hasAnyAuthority(ADMIN_ROLE)
      .anyRequest().authenticated()
  .and()
    .exceptionHandling()
      .authenticationEntryPoint(jwtAuthenticationEntryPoint)
  .and()
    .sessionManagement()
      .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
  .and()
    .formLogin()
      .loginPage(LOGIN_URL)
      .loginProcessingUrl(LOGIN_URL)
      .successForwardUrl(LOGIN_SUCCESS_URL)
      .failureUrl(LOGIN_FAILURE)
  .and()
    .logout()
      .logoutRequestMatcher(new AntPathRequestMatcher(LOGOUT_URL))
      .logoutSuccessUrl(LOGOUT_SUCCESS_URL)
  .and()
    .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
    // in order to allow access to h2-console for testing
    // TODO remove before release
    .headers().frameOptions().disable();
}
```
Ignorando las partes del método que pertenecen al tema de RESTful API y autenticación JWT, se puede ver que mientra various paths están abierto a los usuarios no autenticados, se configura la autenticación del usuario con formulario usando varios variables constantes que se definen en al clase de utilidad `Constants`([GitHub](https://github.com/ChrisHilborne/FilmFanatics/blob/main/src/main/java/io/chilborne/filmfanatic/util/Constants.java)).

#### UserDetailsServiceImpl 
* [UserDetailsServiceImpl](https://github.com/ChrisHilborne/FilmFanatics/blob/main/src/main/java/io/chilborne/filmfanatic/security/UserDetailsServiceImpl.java)

Dado que el objeto `User` implemente el `UserDetails` hubiera sido posible sencillamente implementar la interface `UserDetailsService` con el `UserService`, configurarlo en el `WebSecurityConfig` y de allí llevar todos las funciones del `User` - la autenticación incluida. Pero esto hubiera contradicho el principio de responsabilidades singular de SOLID. Ademas así hubiera sido más difícil de cambiar la manera de autenticación por el fuerte acoplamiento ('coupling' en inglés) entre las partes del programa.    

#### Login/Logout Success

* [header.html](https://github.com/ChrisHilborne/FilmFanatics/blob/main/src/main/resources/templates/fragments/header.html)

Cuando un usuario iniciar sesión con éxito se reenviá a la pagina principal con el url `/?message=success`. El fragmento `header.html` contiene una función de javascript para probar los parámetros del url, y si se detecta el parámetro 'message' con el valor de 'login' se usa la librería de javascript [iziToast](https://izitoast.marcelodolza.com/) para mostrar un mensaje de existo. Lo mismo pasa cuando un usuario termina una sesión solo que el valor del parámetro es 'logout'.   

```
 <script type="text/javascript">
    (function() {
        console.log("checking params");
        let params = new URLSearchParams(window.location.search);
        if (params.has('message')) {
          console.log("params has message.");
          let message = params.get('message');
          if (message == 'logout') {
            console.log("message is logout");
            iziToast.info({
              title: 'Goodbye!',
              message: 'Successfully logged out!',
            });
          }
          else if (message == 'login') {
            console.log("message is login");
            iziToast.success({
              title: 'Hello!',
              message: 'Successfully logged in!',
            });
          }
        }
    })();
```

##### SuccessfulAuthenticationEventListener 
* [SuccessfulAuthenticationEventListener](https://github.com/ChrisHilborne/FilmFanatics/blob/main/src/main/java/io/chilborne/filmfanatic/security/SuccessfulAuthenticationEventListener.java)

El objeto `User` tiene un campo de tipo `LocalDateTime` llamado 'lastLogin' que debe guardar el ultimo fecha y hora que el usuario inicio sesión en el plataforma. Para realizar esta funcionamiento decide utilizar la clase `SuccessfulAuthenticationEventListener` que implemente `ApplicationListener<AuthenticationSuccessEvent> `. De esta manera el método `onApplicationEvent(AuthenticationSuccessEvent event)` se llama cada vez que se autentica con éxito. 
```
  @Override
  public void onApplicationEvent(AuthenticationSuccessEvent event) {
    User user = (User) event.getAuthentication().getPrincipal();
    log.info("User {} successfully logged in", user.getUsername());
    userService.userLoggedIn(user);
  }
```
El método en sí llama otro método de `UserService` llamado `userLoggedIn(User user)` que simplemente actualiza el campo `lastLogin` del `User` y lo guarda en el base de datos.
```
@Override
@Transactional
public void userLoggedIn(User user) {
  LocalDateTime now = LocalDateTime.now();
  User loggedIn = userRepo.findByUsername(user.getUsername()).orElseThrow(UserNotFoundException::new);
  loggedIn.setLastLogin(now);
  userRepo.save(loggedIn);
}
```
#### Login Failure

Si por algún motivo el intento del usuario de iniciar sesión se falla, la pagina de `login.html` se muestra un aviso así.
```
<div th:if="${param.error}" class="alert alert-danger" role="alert">
  Incorrect username or password.
</div>
```

### User Profile

### Delete User

### Edit User

## Películas 