package com.example.novels.novel.repository;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import com.example.novels.novel.entity.Novel;
import com.example.novels.novel.entity.QGenre;
import com.example.novels.novel.entity.QGrade;
import com.example.novels.novel.entity.QNovel;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class SearchNovelRepositoryImpl extends QuerydslRepositorySupport implements SearchNovelRepository {

    public SearchNovelRepositoryImpl() {
        // 기준이 되는 class
        super(Novel.class);
    }

    @Override
    public Object[] getNovelById(Long id) {
        // 하나 조회
        // novel_id, author, title, 대여여부, 장르명, 평점(평균)
        QNovel novel = QNovel.novel;
        QGenre genre = QGenre.genre;
        QGrade grade = QGrade.grade;
        JPQLQuery<Novel> query = from(novel).leftJoin(genre).on(novel.genre.eq(genre)).where(novel.id.eq(id));

        // novel id 별 평점 평균
        JPQLQuery<Double> ratingAvg = JPAExpressions.select(grade.rating.avg()).from(grade).where(grade.novel.eq(novel))
                .groupBy(grade.novel);

        JPQLQuery<Tuple> tuple = query.select(novel, genre, ratingAvg);

        Tuple result = tuple.fetchFirst();
        return result.toArray();
    }

    @Override
    public Page<Object[]> list(Long genreId, String keyword, Pageable pageable) {
        QNovel novel = QNovel.novel;
        QGenre genre = QGenre.genre;
        QGrade grade = QGrade.grade;

        JPQLQuery<Novel> query = from(novel).leftJoin(genre).on(novel.genre.eq(genre));

        // novel id 별 평점 평균
        JPQLQuery<Double> ratingAvg = JPAExpressions.select(grade.rating.avg()).from(grade).where(grade.novel.eq(novel))
                .groupBy(grade.novel);

        JPQLQuery<Tuple> tuple = query.select(novel, genre, ratingAvg);

        BooleanBuilder builder = new BooleanBuilder();
        builder.and((novel.id.gt(0L)));

        // 검색용(장르)
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        if (genreId != null && genreId != 0) {
            booleanBuilder.and(novel.genre.id.eq(genreId));
        }
        // 검색용(title or author)
        if (keyword != null && !keyword.isEmpty()) {
            booleanBuilder.and(novel.title.contains(keyword));
            booleanBuilder.or(novel.author.contains(keyword));
        }
        builder.and(booleanBuilder);
        tuple.where(builder);

        Sort sort = pageable.getSort();

        // sort 기준이 여러개 있을 수 있다
        sort.stream().forEach(order -> {
            Order direction = order.isAscending() ? Order.ASC : Order.DESC;
            String prop = order.getProperty();
            PathBuilder<Novel> orderByExpression = new PathBuilder<>(Novel.class, "novel");
            tuple.orderBy(new OrderSpecifier(direction, orderByExpression.get(prop)));
        });

        // sort 기준이 하나만 존재
        // tuple.orderBy(board.bno.desc());

        // page 처리
        tuple.offset(pageable.getOffset());
        tuple.limit(pageable.getPageSize());
        log.info("===============================");
        log.info(query);
        // select board, member1, count(reply)
        // from Board board
        // left join Member member1 with board.writer = member1
        // left join Reply reply with reply.board = board
        // group by board
        log.info("===============================");

        List<Tuple> result = tuple.fetch();
        long count = tuple.fetchCount(); // 전체개수
        // List<Tuple> => List<Object[]> 변경
        List<Object[]> list = result.stream().map(t -> t.toArray()).collect(Collectors.toList());

        // 리턴값 List<Object[]> list, count, pageable
        return new PageImpl<>(list, pageable, count);
    }
}