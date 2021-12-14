package com.example.book.web;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

// 단위테스트 ( Controller 관련 로직만 띄우기 ) ex) filter, controllerAdvice

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import com.example.book.domain.Book;
import com.example.book.service.BookService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@WebMvcTest
@ExtendWith(SpringExtension.class) // 필수
public class BookControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;
    
    @MockBean //ioc 환경에 bean 등록됨
    private BookService bookService;
    
    //BDD모키토 패턴 - given,when,then 함수 제공
    @Test
    public void save_테스트() throws Exception{
        //given (테스트를 하기 위한 준비)
    	Book book = new Book(null,"스프링따라하기","코스");
    	String content =new ObjectMapper().writeValueAsString(book); //객체를 json으로
    	log.info("save_테스트 : json: "+content);
    	//결과 예상치를 지정해놓음
    	when(bookService.저장하기(book)).thenReturn(new Book(1L,"스프링따라하기","코스"));
    	
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
    	books.add(new Book(1L,"스프링부트따라하기","시기"));
    	books.add(new Book(2L,"리액트따라하기","시기"));
    	when(bookService.모두가져오기()).thenReturn(books);
    	
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
}
