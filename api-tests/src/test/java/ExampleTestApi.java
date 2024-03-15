import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.requestSpecification;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ExampleTestApi {

    @BeforeAll
    public static void beforeAll() {
        RestAssured.baseURI = "http://9b142cdd34e.vps.myjino.ru:49268";
        RestAssured.requestSpecification = given()
                .contentType(ContentType.JSON);
    }

    private String bodyRegister = "{" +
            "\"username\": \"example2\"," +
            "\"password\": \"Qwerty123\"" +
            "}";

    private String bodyProducts = "{" +
            "\"name\": \"exampleBus\"," +
            "\"category\": \"ElectroBus\"" +
            "\"price\": \"13.68\"" +
            "\"discount\": \"4\"" +
            "}";

    @Order(1)
    @Test
    @DisplayName("Регистрация нового пользователя")
    public void postRegister() {
        requestSpecification
                .body(bodyRegister)
                .post("/register")
                .then().assertThat().statusCode(201);
    }

    @Order(2)
    @Test
    @DisplayName("Авторизация пользователя")
    public void postLogin() {
        requestSpecification
                .body(bodyRegister)
                .post("/login")
                .then().assertThat().statusCode(200);

    }

    @Order(1)
    @Test
    @DisplayName("Получение всего списка продуктов")
    public void getProducts() {
        requestSpecification
                .get("/products")
                .then().assertThat().statusCode(200);
    }

    @Order(2)
    @Test
    @DisplayName("Добавление нового продукта")
    public void postProducts() {
        requestSpecification
                .body(bodyProducts)
                .post("/products")
                .then().assertThat().statusCode(405);
    }

    @Order(2)
    @Test
    @DisplayName("Получение списка продуктов по id")
    public void getProductsId() {
        requestSpecification
                .get("/products/1")
                .then().assertThat().statusCode(200);
    }

    @Order(2)
    @Test
    @DisplayName("Обновление списка продуктов по id")
    public void putProductsId() {
        requestSpecification
                .body(bodyProducts)
                .post("/products/1")
                .then().assertThat().statusCode(405);
    }

    @Order(3)
    @Test
    @DisplayName("Удаление списка продуктов по id")
    public void deleteProductsId() {
        requestSpecification
                .post("/products/1")
                .then().assertThat().statusCode(405);
    }

    @Order(3)
    @Test
    @DisplayName("Создание карты покупателя")
    public void postCart() {
        String token = auth();
        requestSpecification
                .headers("Authorization", "Bearer " + token)
                .body("{" +
                        "\"product_id\": 1," +
                        "\"quantity\": 2" +
                        "}")
                .post("/cart")
                .then().assertThat().statusCode(201);
    }

    @Order(4)
    @Test
    @DisplayName("Получение данных по карте покупателя")
    public void getCart() {
        String token = auth();
        requestSpecification
                .headers("Authorization", "Bearer " + token)
                .get("/cart")
                .then().assertThat().statusCode(200);
    }

    @Order(5)
    @Test
    @DisplayName("Удаление сведений с карты покупателя")
    public void deleteProductFromCart() {
        String token = auth();
        requestSpecification
                .headers("Authorization", "Bearer " + token)
                .body("{" +
                        "\"product_id\": 1" +
                        "}")
                .post("/cart/1")
                .then().assertThat().statusCode(405);
    }

    private String auth() {
        return requestSpecification
                .body(bodyRegister)
                .post("/login")
                .then()
                .statusCode(200).extract().jsonPath().get("access_token").toString();
    }
}
