/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.wikift.support.repository.article;

import com.wikift.model.article.ArticleEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
public interface ArticleRepository extends PagingAndSortingRepository<ArticleEntity, Long> {

    @Query(value = "SELECT a.a_id, a.a_title, a.a_content, a.a_create_time, IFNULL(a.view_count, IFNULL(SUM(uavr.uavr_view_count) , 0)) AS view_count, IFNULL(a.fabulou_count, IFNULL(COUNT(DISTINCT uafr.uafr_user_id), 0)) AS fabulou_count, uar.uar_user_id, atr.atr_article_type_id, sar.sar_space_id " +
            "FROM article AS a " +
            "LEFT OUTER JOIN users_article_relation AS uar ON a.a_id = uar.uar_article_id " +
            "LEFT OUTER JOIN article_type_relation AS atr ON a.a_id = atr.atr_article_id " +
            "LEFT OUTER JOIN users_article_view_relation AS uavr ON a.a_id = uavr.uavr_article_id " +
            "LEFT OUTER JOIN users_article_fabulous_relation AS uafr ON a.a_id = uafr.uafr_article_id " +
            "LEFT OUTER JOIN space_article_relation AS sar ON a.a_id = sar.sar_article_id " +
            "LEFT OUTER JOIN space AS s ON s.s_id = sar.sar_space_id " +
            "WHERE s.s_private = FALSE " +
            "GROUP BY a_id, uar.uar_user_id, atr.atr_article_type_id, sar.sar_space_id " +
//            "ORDER BY ?#{#pageable}",
            "ORDER BY SUM(uavr.uavr_view_count) DESC \n#pageable\n",
            countQuery = "SELECT COUNT(a.a_id) FROM article AS a",
            nativeQuery = true)
    Page<ArticleEntity> findAllOrderByViewCount(Pageable pageable);

    @Query(value = "SELECT a.a_id, a.a_title, a.a_content, a.a_create_time, IFNULL(a.view_count, IFNULL(SUM(uavr.uavr_view_count) , 0)) AS view_count, IFNULL(a.fabulou_count, IFNULL(COUNT(DISTINCT uafr.uafr_user_id), 0)) AS fabulou_count, uar.uar_user_id, atr.atr_article_type_id, sar.sar_space_id " +
            "FROM article AS a " +
            "LEFT OUTER JOIN users_article_relation AS uar ON a.a_id = uar.uar_article_id " +
            "LEFT OUTER JOIN article_type_relation AS atr ON a.a_id = atr.atr_article_id " +
            "LEFT OUTER JOIN users_article_view_relation AS uavr ON a.a_id = uavr.uavr_article_id " +
            "LEFT OUTER JOIN users_article_fabulous_relation AS uafr ON a.a_id = uafr.uafr_article_id " +
            "LEFT OUTER JOIN space_article_relation AS sar ON a.a_id = sar.sar_article_id " +
            "LEFT OUTER JOIN space AS s ON s.s_id = sar.sar_space_id " +
            "WHERE s.s_private = FALSE " +
            "GROUP BY a_id, uar.uar_user_id, atr.atr_article_type_id, sar.sar_space_id " +
            "ORDER BY COUNT(uafr.uafr_user_id) DESC \n#pageable\n",
            countQuery = "SELECT COUNT(a.a_id) FROM article AS a",
            nativeQuery = true)
    Page<ArticleEntity> findAllOrderByFabulouCount(Pageable pageable);

    @Query(value = "SELECT a.a_id, a.a_title, a.a_content, a.a_create_time, IFNULL(a.view_count, IFNULL(SUM(uavr.uavr_view_count) , 0)) AS view_count, IFNULL(a.fabulou_count, IFNULL(COUNT(DISTINCT uafr.uafr_user_id), 0)) AS fabulou_count, uar.uar_user_id, atr.atr_article_type_id, sar.sar_space_id " +
            "FROM article AS a " +
            "LEFT OUTER JOIN users_article_relation AS uar ON a.a_id = uar.uar_article_id " +
            "LEFT OUTER JOIN article_type_relation AS atr ON a.a_id = atr.atr_article_id " +
            "LEFT OUTER JOIN users_article_view_relation AS uavr ON a.a_id = uavr.uavr_article_id " +
            "LEFT OUTER JOIN users_article_fabulous_relation AS uafr ON a.a_id = uafr.uafr_article_id " +
            "LEFT OUTER JOIN space_article_relation AS sar ON a.a_id = sar.sar_article_id " +
            "LEFT OUTER JOIN space AS s ON s.s_id = sar.sar_space_id " +
            "WHERE s.s_private = FALSE " +
            "GROUP BY a_id, uar.uar_user_id, atr.atr_article_type_id, sar.sar_space_id " +
            "ORDER BY a.a_create_time DESC \n#pageable\n",
            countQuery = "SELECT COUNT(a.a_id) FROM article AS a",
            nativeQuery = true)
    Page<ArticleEntity> findAllOrderByCreateTime(Pageable pageable);

    /**
     * 根据用户和时间查询文章信息
     *
     * @param username 用户id
     * @return 文章列表
     */
    @Query(value = "SELECT * FROM users_article_relation AS uar, article AS a, users AS u, article_type AS at, article_type_relation AS atr, space_article_relation AS sar " +
            "WHERE uar.uar_user_id = u.u_id " +
            "AND atr.atr_article_id = a.a_id " +
            "AND atr.atr_article_type_id = at.at_id " +
            "AND uar.uar_article_id = a.a_id " +
            "AND a.a_id = sar.sar_article_id " +
            "AND DATE_SUB(CURDATE() , INTERVAL 7 DAY) <= date(a.a_create_time) " +
            "AND u.u_username = ?1", nativeQuery = true)
    List<ArticleEntity> findTopByUserEntityAndCreateTime(String username);

    /**
     * 赞文章
     *
     * @param userId    当前赞用户id
     * @param articleId 当前被赞文章id
     * @return 状态
     */
    @Modifying
    @Query(value = "INSERT INTO users_article_fabulous_relation(uafr_user_id, uafr_article_id) " +
            "VALUE(?1, ?2)",
            nativeQuery = true)
    Integer fabulousArticle(Integer userId, Integer articleId);

    /**
     * 解除赞文章
     *
     * @param userId    当前解除赞用户id
     * @param articleId 当前解除赞文章id
     * @return 状态
     */
    @Modifying
    @Query(value = "DELETE FROM users_article_fabulous_relation " +
            "WHERE uafr_user_id = ?1 " +
            "AND uafr_article_id = ?2",
            nativeQuery = true)
    Integer unFabulousArticle(Integer userId, Integer articleId);

    /**
     * 检查当前文章是否被当前用户赞
     *
     * @param userId    当前赞用户id
     * @param articleId 当前被赞文章id
     * @return 数据总数
     */
    @Query(value = "SELECT count(1) AS count FROM users_article_fabulous_relation " +
            "WHERE uafr_user_id = ?1 " +
            "AND uafr_article_id = ?2",
            nativeQuery = true)
    Integer findFabulousArticleExists(Integer userId, Integer articleId);

    /**
     * 当前文章被赞用户总数
     *
     * @param articleId 当前被赞文章id
     * @return 数据总数
     */
    @Query(value = "SELECT count(1) AS count FROM users_article_fabulous_relation " +
            "WHERE uafr_article_id = ?1",
            nativeQuery = true)
    Integer findFabulousArticleCount(Integer articleId);

    /**
     * 用户浏览文章信息
     *
     * @param userId     当前浏览文章的用户id
     * @param articleId  当前浏览的文章id
     * @param viewCount  浏览文章的总数
     * @param viewDevice 浏览文章的用户设备(用于区分为做bi准备)
     * @return 数据状态
     */
    @Modifying
    @Query(value = "INSERT INTO users_article_view_relation(uavr_user_id, uavr_article_id, uavr_view_count, uavr_view_device) " +
            "VALUES (?1, ?2, ?3, ?4)",
            nativeQuery = true)
    Integer viewArticle(Integer userId, Integer articleId, Integer viewCount, String viewDevice);

    /**
     * 更新文章的访问数据总数
     *
     * @param userId     当前浏览文章的用户id
     * @param articleId  当前浏览的文章id
     * @param viewCount  浏览文章的总数
     * @param viewDevice 浏览文章的用户设备(用于区分为做bi准备)
     * @return
     */
    @Modifying
    @Query(value = "UPDATE users_article_view_relation " +
            "SET uavr_user_id = ?1, uavr_article_id = ?2, uavr_view_count = ?3, uavr_view_device = ?4 " +
            "WHERE uavr_user_id = ?1 " +
            "AND uavr_article_id = ?2 " +
            "AND uavr_view_device = ?4",
            nativeQuery = true)
    Integer updateViewArticle(Integer userId, Integer articleId, Integer viewCount, String viewDevice);

    /**
     * 根据用户访问的设备进行查询访问数据总数
     *
     * @param userId     当前浏览文章的用户id
     * @param articleId  当前浏览的文章id
     * @param viewDevice 浏览文章的用户设备(同一用户使用多设备访问, 标识为多设备)
     * @return 当前用户, 设备对当前文章的访问量总数
     */
    @Query(value = "SELECT SUM(uavr_view_count) FROM users_article_view_relation " +
            "WHERE uavr_user_id = ?1 " +
            "AND uavr_article_id = ?2 " +
            "AND uavr_view_device = ?3",
            nativeQuery = true)
    Integer findViewArticleByDevice(Integer userId, Integer articleId, String viewDevice);

    /**
     * 根据用户访问的设备进行查询访问数据总数
     *
     * @param userId    当前浏览文章的用户id
     * @param articleId 当前浏览的文章id
     * @return 对当前文章的访问量总数
     */
    @Query(value = "SELECT SUM(uavr_view_count) FROM users_article_view_relation " +
            "WHERE uavr_user_id = ?1 " +
            "AND uavr_article_id = ?2",
            nativeQuery = true)
    Integer findViewArticle(Integer userId, Integer articleId);

}
