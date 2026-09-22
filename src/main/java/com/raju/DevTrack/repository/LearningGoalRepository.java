package com.raju.DevTrack.repository;

import com.raju.DevTrack.model.LearningGoal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LearningGoalRepository extends JpaRepository<LearningGoal,Long> , JpaSpecificationExecutor<LearningGoal> {


    Page<LearningGoal> findAll(Specification specification,Pageable pageable);

    List<LearningGoal> findAllByUser_Username(String username);
}
