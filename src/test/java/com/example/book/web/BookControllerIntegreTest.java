package com.example.book.web;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

// 통합테스트 ( 모든 Bean 들을 ioc에 올리고 테스트 하는 것)

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;

import com.example.book.domain.Book;
import com.example.book.domain.BookRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

// WebEnvironment.RANDOM_PORT : 실제 톰캣으로 테스트
// WebEnvironment.MOCK :실제 톰캣을 올리는 것이 아니라 다른 톰캣으로 테스트
// @AutoConfigureMockMvc : MockMvc를 ioc 등록해준다.
// @Transactional : 각각의 테스트 함수가 종료될 때마다 트랜잭션을 rollback 해주는 어노테이션
@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class BookControllerIntegreTest {
    @Autowired
    private MockMvc mockMvc;    // 컨트롤러의 주소로 테스트해볼 수 있는 라이브러리
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private EntityManager entityManager;
    @BeforeEach  //모든 테스트에 각각 실행됨 (AUTO_INCREMENT 충돌때문에 설정함)
    public void init() {
    	entityManager.createNativeQuery("ALTER TABLE book ALTER COLUMN id RESTART WITH 1").executeUpdate();
    }
    
    @Test
    public void save_테스트() throws Exception{
        //given (테스트를 하기 위한 준비)
    	Book book = new Book(null,"스프링따라하기","코스");
    	String content =new ObjectMapper().writeValueAsString(book); //객체를 json으로
    	
    	// when ( 테스트 실행 )
    	ResultActions resultActions = mockMvc.perform(post("/book")
    			.contentType(MediaType.APPLICATION_JSON_UTF8)
    			.content(content)
    			.accept(MediaType.APPLICATION_JSON_UTF8));
    	// then ( 검증 )
    	resultActions
    	.andExpect(status().isCreated())
    	.andExpect(jsonPath("$.title").value("스프링따라하기")) //결과값의 title이 "스프링따라하기"인지 truefalse
    	.andDo(MockMvcResultHandlers.print());
    }
    @Test
    public void findAll_test() throws Exception{
    	// given
    	List<Book> books = new ArrayList<>();
    	books.add(new Book(null,"스프링부트따라하기","시기"));
    	books.add(new Book(null,"리액트따라하기","시기"));
    	bookRepository.saveAll(books);
    	
    	// when
    	ResultActions resultActions = mockMvc.perform(get("/book")
    										.accept(MediaType.APPLICATION_JSON_UTF8));
    	// then
    	resultActions
    	.andExpect(status().isOk())
    	.andExpect(jsonPath("$", Matchers.hasSize(2)))
        .andExpect(jsonPath("$.[0].title").value("스프링부트따라하기"))
        .andDo(MockMvcResultHandlers.print());
    }
    @Test
    public void findById_테스트() throws Exception{
    	// given
    	Long id = 1L;
    	List<Book> books = new ArrayList<>();
    	books.add(new Book(null,"스프링부트따라하기","시기"));
    	books.add(new Book(null,"리액트따라하기","시기"));
    	bookRepository.saveAll(books);
    
    	// when
    	ResultActions resultActions = mockMvc.perform(get("/book/{id}",id)
    										.accept(MediaType.APPLICATION_JSON_UTF8));
    	// then
    	resultActions
    	.andExpect(status().isOk())
        .andExpect(jsonPath("$.title").value("스프링부트따라하기"))
        .andDo(MockMvcResultHandlers.print());
    }
    @Test
    public void update_테스트() throws Exception{
    	// given
    	
    	Long id = 1L;
    	List<Book> books = new ArrayList<>();
    	books.add(new Book(null,"스프링부트따라하기","시기"));
    	books.add(new Book(null,"리액트따라하기","시기"));
    	bookRepository.saveAll(books);
    	Book book = new Book(null,"c++따라하기","시기");
    	String content =new ObjectMapper().writeValueAsString(book); //객체를 json으로
    	
    	// when
    	ResultActions resultActions = mockMvc.perform(put("/book/{id}",id)
    										.contentType(MediaType.APPLICATION_JSON_UTF8)
    										.content(content)
    										.accept(MediaType.APPLICATION_JSON_UTF8));
    	// then
    	resultActions
    	.andExpect(status().isOk())
        .andExpect(jsonPath("$.title").value("c++따라하기"))
        .andDo(MockMvcResultHandlers.print());
    }
    @Test
    public void delete_테스트() throws Exception{
    	// given
    	Long id = 1L;
    	List<Book> books = new ArrayList<>();
    	books.add(new Book(null,"스프링부트따라하기","시기"));
    	books.add(new Book(null,"리액트따라하기","시기"));
    	bookRepository.saveAll(books);
    
    	// when
    	ResultActions resultActions = mockMvc.perform(delete("/book/{id}",id)
    										.accept(MediaType.TEXT_PLAIN));
    	// then
    	resultActions
    	.andExpect(status().isOk())
        .andDo(MockMvcResultHandlers.print());
    	
    	MvcResult requestResult =resultActions.andReturn();
    	String result = requestResult.getResponse().getContentAsString();
    	assertEquals("ok",result);
    }
}
