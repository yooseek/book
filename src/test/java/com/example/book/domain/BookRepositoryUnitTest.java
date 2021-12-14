package com.example.book.domain;

// 단위테스트 ( DB 관련 로직만 띄우기 )

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

// Replace.ANY :가짜 디비로 테스트
// Replace.NONE :실제 디비로 테스트
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@DataJpaTest    //Repository들을 다 IOC등록해줌
public class BookRepositoryUnitTest {

    @Autowired
    private BookRepository bookRepository;

}
