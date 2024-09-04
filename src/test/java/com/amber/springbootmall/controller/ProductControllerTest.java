package com.amber.springbootmall.controller;

import com.amber.springbootmall.constant.ProductCategory;
import com.amber.springbootmall.dto.ProductRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    //查詢商品
    //情況一、抓取商品成功
    @Test
    public void getProduct_success() throws Exception {
        //創建Http request
        RequestBuilder  requestBuilder = MockMvcRequestBuilders
                .get("/products/{productId}",1);

        //執行Http request
        mockMvc.perform(requestBuilder)
                .andDo(print()) //將值印出
                // 回傳狀態為成功
                .andExpect(status().isOk())
                //因為傳送值為json格式的關係，因此需要使用jsonPath()來驗證回應中的 JSON 結構和內容
                .andExpect(jsonPath("$.productName", equalTo("蘋果（澳洲）")))
                .andExpect(jsonPath("$.category", equalTo("FOOD")))
                .andExpect(jsonPath("$.imageUrl", notNullValue()))
                .andExpect(jsonPath("$.price", notNullValue()))
                .andExpect(jsonPath("$.stock", notNullValue()))
                .andExpect(jsonPath("$.description", notNullValue()))
                .andExpect(jsonPath("$.createdDate", notNullValue()))
                .andExpect(jsonPath("$.lastModifiedDate", notNullValue()));
    }
    //情況二、抓取商品失敗
    @Test
    public void getProduct_notFound() throws Exception {
        //創建Http request(id抓取不存在的)
        RequestBuilder  requestBuilder = MockMvcRequestBuilders
                .get("/products/{productId}",4423);

        //執行Http request
        mockMvc.perform(requestBuilder)
                .andExpect(status().isNotFound());
    }

    //創建商品
    //情況一、新增商品成功
    @Test
    @Transactional
    public void createProduct_success() throws Exception {

        //創建商品資料(使用dto)
        ProductRequest productRequest = new ProductRequest();
        productRequest.setProductName("好吃的麵包");
        productRequest.setCategory(ProductCategory.FOOD);
        productRequest.setImageUrl("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR9_Z0HWXijmabomScz1kgKDWFC0N-cCi2Y1g&s");
        productRequest.setStock(100);
        productRequest.setPrice(25);

        //將java object轉為json格式
        String json = objectMapper.writeValueAsString(productRequest);

        //設定Http request資訊
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        //執行Http request
        mockMvc.perform(requestBuilder)
                .andExpect(status().is(201))
                .andExpect(jsonPath("$.productName", equalTo("好吃的麵包")))
                .andExpect(jsonPath("$.category", equalTo("FOOD")))
                .andExpect(jsonPath("$.imageUrl", equalTo("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR9_Z0HWXijmabomScz1kgKDWFC0N-cCi2Y1g&s")))
                .andExpect(jsonPath("$.price", equalTo(25)))
                .andExpect(jsonPath("$.stock", equalTo(100)))
                .andExpect(jsonPath("$.description", nullValue()))
                .andExpect(jsonPath("$.createdDate", notNullValue()))
                .andExpect(jsonPath("$.lastModifiedDate", notNullValue()));
    }
    //情況二、資料未備齊
    @Test
    @Transactional
    public void createProduct_illegalArgument() throws Exception {
        //創建商品資料(使用dto)
        ProductRequest productRequest = new ProductRequest();
        productRequest.setProductName("好吃的麵包");

        //將java object轉為json格式
        String json = objectMapper.writeValueAsString(productRequest);

        //設定Http request資訊
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        //執行Http request
        mockMvc.perform(requestBuilder)
                .andExpect(status().is(400));

    }
    // 查詢商品列表
    //情況一、正常的查詢所有商品
    @Test
    public void getProducts() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/products");

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.limit", notNullValue()))
                .andExpect(jsonPath("$.offset", notNullValue()))
                .andExpect(jsonPath("$.total", notNullValue()))
                .andExpect(jsonPath("$.results", hasSize(5)));
    }

    //情況二、查詢條件使否有回應符合條件的值
    @Test
    public void getProducts_filtering() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/products")
                .param("search", "B")
                .param("category", "CAR");

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.limit", notNullValue()))
                .andExpect(jsonPath("$.offset", notNullValue()))
                .andExpect(jsonPath("$.total", notNullValue()))
                .andExpect(jsonPath("$.results", hasSize(2)));
    }

    //情況三、查看排序狀況是否有符合
    @Test
    public void getProducts_sorting() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/products")
                .param("orderBy", "price")
                .param("sort", "desc");

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.limit", notNullValue()))
                .andExpect(jsonPath("$.offset", notNullValue()))
                .andExpect(jsonPath("$.total", notNullValue()))
                .andExpect(jsonPath("$.results", hasSize(5)))
                .andExpect(jsonPath("$.results[0].productId", equalTo(6)))
                .andExpect(jsonPath("$.results[1].productId", equalTo(5)))
                .andExpect(jsonPath("$.results[2].productId", equalTo(7)))
                .andExpect(jsonPath("$.results[3].productId", equalTo(4)))
                .andExpect(jsonPath("$.results[4].productId", equalTo(2)));
    }

    //情況四、利用限制值去查出現的值是否符合
    @Test
    public void getProducts_pagination() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/products")
                .param("limit", "2")
                .param("offset", "2");

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.limit", notNullValue()))
                .andExpect(jsonPath("$.offset", notNullValue()))
                .andExpect(jsonPath("$.total", notNullValue()))
                .andExpect(jsonPath("$.results", hasSize(2)))
                .andExpect(jsonPath("$.results[0].productId", equalTo(5)))
                .andExpect(jsonPath("$.results[1].productId", equalTo(4)));
    }

}