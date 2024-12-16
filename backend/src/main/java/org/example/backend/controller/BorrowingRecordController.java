package org.example.backend.controller;

import org.example.backend.model.BorrowingRecord;
import org.example.backend.service.BorrowingRecordService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/borrowing-records")
public class BorrowingRecordController {

    private final BorrowingRecordService borrowingRecordService;

    public BorrowingRecordController(BorrowingRecordService borrowingRecordService) {
        this.borrowingRecordService = borrowingRecordService;
    }

    @GetMapping
    public List<BorrowingRecord> getAllBorrowingRecords() {
        return borrowingRecordService.getAllRecords();
    }

    @PostMapping
    public ResponseEntity<BorrowingRecord> createRecord(@RequestBody BorrowingRecord record) {
        return ResponseEntity.ok(borrowingRecordService.saveRecord(record));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecord(@PathVariable Long id) {
        if (borrowingRecordService.deleteRecord(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
