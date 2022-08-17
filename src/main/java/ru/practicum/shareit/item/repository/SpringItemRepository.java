package ru.practicum.shareit.item.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface SpringItemRepository extends JpaRepository<Item, Long> {
    List<Item> findAllByOwner(Long userId);

    List<Item> findAllByOwner(Long userId, Pageable pageable);

    @Query(value = "select * from items as it where it.name" +
            "  like concat('%', ?1, '%') or it.description" +
            "  like concat('%', ?2, '%') and available=true",nativeQuery = true)
    List<Item> searchByNameAndDescription (String name, String description, Pageable pageable);
}
