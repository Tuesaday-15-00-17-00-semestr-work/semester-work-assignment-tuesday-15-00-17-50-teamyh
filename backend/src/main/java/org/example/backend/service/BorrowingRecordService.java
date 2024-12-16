package org.example.backend.service;

import org.example.backend.model.BorrowingRecord;
import org.example.backend.repository.BorrowingRecordRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class BorrowingRecordService {

    private final BorrowingRecordRepository borrowingRecordRepository;

    public BorrowingRecordService(BorrowingRecordRepository borrowingRecordRepository) {
        this.borrowingRecordRepository = borrowingRecordRepository;
    }

    /**
     * Получить все записи
     * @return Список всех записей
     */
    @Transactional(readOnly = true)
    public List<BorrowingRecord> getAllRecords() {
        return borrowingRecordRepository.findAll();
    }

    /**
     * Получить запись по ID
     * @param id ID записи
     * @return Optional с записью или пустой Optional, если запись не найдена
     */
    @Transactional(readOnly = true)
    public Optional<BorrowingRecord> getRecordById(Long id) {
        return borrowingRecordRepository.findById(id);
    }

    /**
     * Сохранить или обновить запись
     * @param record Запись для сохранения
     * @return Сохранённая запись
     */
    @Transactional
    public BorrowingRecord saveRecord(BorrowingRecord record) {
        return borrowingRecordRepository.save(record);
    }

    /**
     * Удалить запись по ID
     * @param id ID записи
     * @return true, если запись была успешно удалена, иначе false
     */
    @Transactional
    public boolean deleteRecord(Long id) {
        if (borrowingRecordRepository.existsById(id)) {
            borrowingRecordRepository.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * Проверить, существует ли запись по ID
     * @param id ID записи
     * @return true, если запись существует, иначе false
     */
    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        return borrowingRecordRepository.existsById(id);
    }
}
