package com.example.book.service;

// 단위테스트 ( service 관련 로직만 메모리에 띄우기 )

import com.example.book.domain.BookRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/*
* BookRepository => 가짜객체를 Mockito 환경이 생성해줌
*/
@ExtendWith(MockitoExtension.class)
public class BookServiceUnitTest {

    @InjectMocks  // 해당파일에 @Mock로 등록된 모든 애들을 주입받는다.
    private BookService bookService;
    
    @Mock
    private BookRepository bookRepository;
}
