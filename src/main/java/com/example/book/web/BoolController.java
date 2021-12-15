package com.example.book.web;

import com.example.book.domain.Book;
import com.example.book.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class BoolController {

    private final BookService bookService;
    @CrossOrigin //자바 스크립트 요청 허용
    @GetMapping("/book")
    public ResponseEntity<?> findAll(){
        return new ResponseEntity<>(bookService.모두가져오기(), HttpStatus.OK);
    }

    @GetMapping("/book/{id}")
    public ResponseEntity<?> findAll(@PathVariable Long id){
        return new ResponseEntity<>(bookService.한건가져오기(id), HttpStatus.OK);
    }
    @CrossOrigin //자바 스크립트 요청 허용 , 컨트롤러 진입직전
    @PostMapping("/book")
    public ResponseEntity<?> save(@RequestBody Book book){
        return new ResponseEntity<>(bookService.저장하기(book), HttpStatus.CREATED);
    }
    @PutMapping("/book/{id}")
    public ResponseEntity<?> update(@PathVariable Long id,@RequestBody Book book){
        return new ResponseEntity<>(bookService.수정하기(id,book), HttpStatus.OK);
    }
    @DeleteMapping("/book/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        return new ResponseEntity<>(bookService.삭제하기(id), HttpStatus.OK);
    }



}
