package ru.practicum.shareit.item.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface SpringItemRepository extends JpaRepository<Item, Long> {
    List<Item> findAllByOwner(Long userId);

    List<Item> findAllByOwner(Long userId, Pageable pageable);

    List<Item> findAllByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndAvailableTrue(String name, String description, Pageable pageable);
}
