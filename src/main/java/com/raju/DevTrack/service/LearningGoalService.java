package com.raju.DevTrack.service;


import com.raju.DevTrack.dto.LearningGoalRequest;
import com.raju.DevTrack.dto.LearningGoalResponse;
import com.raju.DevTrack.dto.PaginatedGoalResponse;
import com.raju.DevTrack.exceptions.ResourceNotFoundException;
import com.raju.DevTrack.exceptions.UserNotMatched;
import com.raju.DevTrack.model.GoalStatus;
import com.raju.DevTrack.model.LearningGoal;
import com.raju.DevTrack.model.User;
import com.raju.DevTrack.repository.LearningGoalRepository;
import com.raju.DevTrack.repository.UserRepository;
import com.raju.DevTrack.specification.LearningGoalSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LearningGoalService {

    private final LearningGoalRepository learningGoalRepository;

    private final UserRepository userRepository;

    public LearningGoalResponse createGoal(LearningGoalRequest learningGoalRequest){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String username=authentication.getName();
        User user=userRepository.findUserByUsername(username).orElseThrow(()->new ResourceNotFoundException("User with username " + username + " not found"));
        GoalStatus status=determineStatus(learningGoalRequest.getProgress());
        LearningGoal learningGoal=new LearningGoal(null,user,learningGoalRequest.getName(),learningGoalRequest.getDescription(),learningGoalRequest.getStartDate(),status,learningGoalRequest.getProgress());
        LearningGoal savedGoal= learningGoalRepository.save(learningGoal);
        return mapToResponse(savedGoal);
    }

    public PaginatedGoalResponse getAllGoals(GoalStatus status, String SortBy, String direction, int page, int size, String search, Integer minProgress, Integer maxProgress,LocalDate startDateFrom,LocalDate startDateTo){
        Sort sort=direction.equalsIgnoreCase("desc")?Sort.by(SortBy).descending():Sort.by(SortBy).ascending();
        Pageable pageable= PageRequest.of(page,size,sort);
        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
        String username= authentication.getName();
        Specification<LearningGoal> specification=Specification.unrestricted();
        if(status!=null)
            specification=specification.and(LearningGoalSpecification.hasStatus(status));
        if(search!=null && !search.isBlank())
            specification=specification.and(LearningGoalSpecification.containsSearch(search));
        if(minProgress!=null){
            specification=specification.and(LearningGoalSpecification.hasMinProgress(minProgress));
        }
        if(maxProgress!=null){
            specification=specification.and(LearningGoalSpecification.hasMaxProgress(maxProgress));
        }
        if(startDateFrom!=null){
            specification=specification.and(LearningGoalSpecification.hasStartDateFrom(startDateFrom));
        }
        if(startDateTo!=null){
            specification=specification.and(LearningGoalSpecification.hasStartDateTo(startDateTo));
        }
        if(username!=null){
            specification=specification.and(LearningGoalSpecification.hasUsername(username));
        }
        Page<LearningGoal> goalsPage=learningGoalRepository.findAll(specification,pageable);
        List<LearningGoalResponse> goalsResponse= new ArrayList<>();
        for(LearningGoal goal:goalsPage.getContent()){
            goalsResponse.add(mapToResponse(goal));
        }
        return new PaginatedGoalResponse(goalsResponse,goalsPage.getNumber(),goalsPage.getSize(),goalsPage.getTotalElements(),goalsPage.getTotalPages(),goalsPage.isFirst(),goalsPage.isLast());
    }

    public LearningGoalResponse getGoalById(Long id){
        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
        String username=authentication.getName();
        LearningGoal goal= learningGoalRepository.findById(id).orElse(null);
        if(goal==null) throw new ResourceNotFoundException("Goal with id " + id + " not found");
        if(username.equals(goal.getUser().getUsername()))
        return mapToResponse(goal);
        else throw new UserNotMatched("Username "+username+" and Goal with id "+goal.getId()+" not matched");
    }

    public LearningGoalResponse updateGoal(Long id,LearningGoalRequest learningGoalRequest){
        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
        String username=authentication.getName();
        int progress=learningGoalRequest.getProgress();
        GoalStatus status=determineStatus(progress);
        LearningGoal existingGoal=learningGoalRepository.findById(id).orElse(null);
        if(existingGoal==null){
            throw new ResourceNotFoundException("Goal with id " + id + " not found");
        }
        if(!username.equals(existingGoal.getUser().getUsername())) {
            throw new UserNotMatched("Username " + username + " and Goal with id " + existingGoal.getId() + " not matched");
        }
                existingGoal.setName(learningGoalRequest.getName());
                existingGoal.setDescription(learningGoalRequest.getDescription());
                existingGoal.setStartDate(learningGoalRequest.getStartDate());
                existingGoal.setStatus(status);
                existingGoal.setProgress(progress);
                LearningGoal updatedGoal = learningGoalRepository.save(existingGoal);
                return mapToResponse(updatedGoal);
            }

    public LearningGoalResponse deleteGoal(Long id){
        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
        String username=authentication.getName();
        LearningGoal deletingGoal=learningGoalRepository.findById(id).orElse(null);

        if(deletingGoal==null) {
            throw new ResourceNotFoundException("Goal with id " + id + " not found");
        }
        if(!username.equals(deletingGoal.getUser().getUsername())){
            throw new UserNotMatched("Username " + username + " and Goal with id " + deletingGoal.getId() + " not matched");
        }
        learningGoalRepository.deleteById(id);
        return mapToResponse(deletingGoal);

    }
    private LearningGoalResponse mapToResponse(LearningGoal goal){
        return new LearningGoalResponse(goal.getId(),goal.getName(),goal.getDescription(),goal.getStartDate(),goal.getStatus(),goal.getProgress());
    }
    private GoalStatus determineStatus(int progress){
        if(progress==0)
            return  GoalStatus.NOT_STARTED;
        else if(progress==100)
            return GoalStatus.COMPLETED;
        else
            return GoalStatus.IN_PROGRESS;
    }

}
