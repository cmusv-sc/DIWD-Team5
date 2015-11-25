package neo4j.repositories;

import neo4j.domain.Paper;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Repository
public interface PaperRepository extends GraphRepository<Paper> {
    Paper findByTitle(@Param("title") String title);

    @Query("MATCH (p:Paper)<-[:PUBLISH]-(a:Author) WHERE p.title =~ ('(?i).*'+{title}+'.*') RETURN p.title as paper, collect(a.name) as cast LIMIT 50")
    List<Map<String, Object>> findByTitleContaining(@Param("title") String title);

    @Query("MATCH (p:Paper)<-[:PUBLISH]-(a:Author) RETURN p.title as paper, collect(a.name) as cast LIMIT {limit}")
    List<Map<String, Object>> graph(@Param("limit") int limit);
    
    @Query("MATCH (p:Paper)<-[:PUBLISH]-(a:Author) WHERE a.name = {name} RETURN p.title as paper, collect(a.name) as cast LIMIT 50")
    List<Map<String, Object>> getPaperByAuthor(@Param("name") String name);
    
    @Query("MATCH (a1:Author)<-[:CO]-(a2:Author) WHERE a2.name = {name} RETURN a1.name as coauthor, collect(a2.name) as cast LIMIT 50")
    List<Map<String, Object>> getCoAuthor(@Param("name") String name);
    
    
}


