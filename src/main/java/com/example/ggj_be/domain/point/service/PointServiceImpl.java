package com.example.ggj_be.domain.point.service;

import com.example.ggj_be.domain.enums.PointType;
import com.example.ggj_be.domain.member.Member;
import com.example.ggj_be.domain.member.repository.MemberRepository;
import com.example.ggj_be.domain.point.Point;
import com.example.ggj_be.domain.point.dto.CommentRequest;
import com.example.ggj_be.domain.point.dto.PointCreateRequest;
import com.example.ggj_be.domain.point.dto.PointUpateRequest;
import com.example.ggj_be.domain.point.repository.PointRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class PointServiceImpl implements PointService {

    @Autowired
    private PointRepository pointRepository;

    @Autowired
    private MemberRepository memberRepository;



    @Override
    public List<Point> getUserList(PointType pointType) {
        List<Point> userList = pointRepository.findByPointTypeAndAccAtIsNullOrderByCreatedAtAsc(pointType);
        return userList;
    }

    @Override
    public List<Member> getAllUserList() {
        List<Member> userAllList = memberRepository.findAll();
        return userAllList;
    }

    @Override
    public Boolean createPoint(Member member, PointCreateRequest request) {

        try{
            Member isMember = memberRepository.findById(member.getUserId())
                    .orElseThrow(() -> new RuntimeException("Member not found"));

            if(request.getPointType() == PointType.remove) {
                Long currentPoint = Optional.ofNullable(isMember.getPoint()).orElse(0L);

                if(currentPoint < request.getChangePoint()) {
                    return false;
                }
            }

            Point point = Point.builder()
                    .member(isMember)
                    .changePoint(request.getChangePoint())
                    .pointType(request.getPointType())
                    .build();
            pointRepository.save(point);
        }catch (Exception e){
            log.error(" Error creating point", e);
            throw new RuntimeException("포인트 생성 실패", e);

        }
        return true;
    }


    @Override
    public Boolean updatePoint(PointUpateRequest request) {

        try{
            Member member = memberRepository.findById(request.getUserId())
                    .orElseThrow(() -> new RuntimeException("Member not found"));

            member.setPoint(request.getChangePoint(), request.getPointType());
            memberRepository.save(member);

            if (!"userAllList".equals(request.getListType())) {
                Point point = pointRepository.findById(request.getPointId())
                        .orElseThrow(() -> new RuntimeException("Point not found"));

                point.setAccAt();
                pointRepository.save(point);
            }

        }catch (Exception e){
            log.error(" Error update point", e);
            throw new RuntimeException("포인트 수정 실패", e);

        }
        return true;
    }


    @Override
    public Boolean updateComment(CommentRequest request) {

        try{

            if ("userAllList".equals(request.getListType())){
                Member member = memberRepository.findById(request.getUserId())
                        .orElseThrow(() -> new RuntimeException("Member not found"));

                member.setUserComment(request.getComment());
                memberRepository.save(member);
            } else {
                Point point = pointRepository.findById(request.getPointId())
                        .orElseThrow(() -> new RuntimeException("Point not found"));

                point.setPointComment(request.getComment());
                pointRepository.save(point);
            }

        }catch (Exception e){
            log.error(" Error update point", e);
            throw new RuntimeException("포인트 수정 실패", e);

        }
        return true;
    }


    @Override
    public List<Point> getPointList(Member member, String period) {

        try{
            LocalDate now = LocalDate.now();
            
            LocalDateTime startDate = null;
            switch (period) {
                case "1w":
                    startDate = now.minusWeeks(1).atStartOfDay(); // 시간까지 포함한 LocalDateTime으로 변환
                    break;
                case "3m":
                    startDate = now.minusMonths(3).atStartOfDay();
                    break;
                case "6m":
                    startDate = now.minusMonths(6).atStartOfDay();
                    break;
                case "all":
                    return pointRepository.findByMember_UserIdAndAccAtIsNotNull(member.getUserId());
            }
            List<Point> pointList = pointRepository.findByMember_UserIdAndCreatedAtAfterAndAccAtIsNotNull(member.getUserId(), startDate); 
            return pointList;
            
        }catch (Exception e){
            log.error(" Error getPointList", e);
            throw new RuntimeException("포인트내역 불러오기 실패!", e);
        }
        
    }
    
}
