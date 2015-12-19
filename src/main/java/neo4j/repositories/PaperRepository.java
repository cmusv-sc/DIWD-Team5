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
    
    @Query("MATCH (p:Paper)<-[:PUBLISH]-(a:Author) WHERE p.title =~ ('(?i).*'+{title}+'.*') RETURN a.name as paper, collect(a.name) as cast LIMIT 50")
    List<Map<String, Object>> findByTitleContaining1(@Param("title") String title);
    
    @Query("MATCH (p:Paper)  WHERE p.title =~ ('(?i).*'+{title}+'.*') RETURN p.title as paper, collect(p.title) as cast LIMIT 50")
    List<Map<String, Object>> findPaperByTitleContaining(@Param("title") String title);
    
    @Query("MATCH (p:Paper)<-[:PUBLISH]-(a:Author) RETURN p.title as paper, collect(a.name) as cast LIMIT {limit}")
    List<Map<String, Object>> graph(@Param("limit") int limit);
    
    @Query("MATCH (p:Paper)<-[:PUBLISH]-(a:Author) WHERE a.name = {name} RETURN p.title as paper, collect(a.name) as cast LIMIT 50")
    List<Map<String, Object>> getPaperByAuthor(@Param("name") String name);
    
    @Query("MATCH (a1:Author)<-[:CO]-(a2:Author) WHERE a2.name = {name} RETURN a2.name as author, collect(a1.name) as cast LIMIT 50")
    List<Map<String, Object>> getCoAuthor(@Param("name") String name);
    
    @Query("MATCH (p:Paper)<-[:PUBLISH]-(a:Author) WHERE a.name = {name} AND p.year > {begYear} AND p.year < {endYear} RETURN p.title as paper, collect(a.name) as cast LIMIT 50")
    List<Map<String, Object>> getPaperByAuthorAndTimeline(@Param("name") String name, @Param("begYear") Integer begYear, @Param("endYear") Integer endYear);

    @Query("MATCH (p:Paper)<-[:PUBLISH]-(a:Author) WHERE p.year > {begYear} AND p.year < {endYear} RETURN p.title as paper, collect(a.name) as cast LIMIT 50")
    List<Map<String, Object>> getPaperByTimeline(@Param("begYear") Integer begYear, @Param("endYear") Integer endYear);
    
    // new query :
    @Query("MATCH (p1:Paper)<-[:CITE]-(p2:Paper) WHERE p1.title = {name} RETURN p1.title as paper, collect(p2.title) as cast LIMIT 50")
    List<Map<String, Object>> getCitationByPaper(@Param("name") String name);
    
    @Query("MATCH (p1:Paper)<-[:CITE]-(p2:Paper) WHERE p2.title =~ ('(?i).*'+{name}+'.*') RETURN p1.title as paper, collect(p2.title) as cast LIMIT 50")
    List<Map<String, Object>> getCitationByKeyword(@Param("name") String name);
    
    @Query("MATCH (p:Paper)<-[:PUBLISH]-(a:Author) WHERE p.journal = {name} RETURN p.title as paper, collect(a.name) as cast LIMIT 50")
    List<Map<String, Object>> getPaperByJournal(@Param("name") String name);
    
    @Query("MATCH (p:Paper)<-[:PUBLISH]-(a:Author) WHERE p.title = {name} RETURN a.name as name, collect(p.volume) as cast LIMIT 50")
    List<Map<String, Object>> getVolumeByTitle(@Param("name") String name);
    
    @Query("MATCH (p:Paper)<-[:PUBLISH]-(a:Author) WHERE p.title = {name} RETURN a.name as author, collect(p.title) as cast LIMIT 50")
    List<Map<String, Object>> getAuthorByTitle(@Param("name") String name);
    
    @Query("MATCH (p1:Paper)<-[:CITE]-(p2:Paper) WHERE p1.journal = {name} AND p1.year = {year} RETURN p1.title as paper, collect(p2.title) as cast LIMIT 50")
    List<Map<String, Object>> findTopCitedPaper(@Param("name") String name, @Param("year") Integer year);
    
    @Query("match (a:Author {name: {author1}}), (b:Author {name: {author2}}), p = shortestPath((a)-[*..10]-(b)) with extract(n IN nodes(p)| n.name)"
    		+ " as Authors unwind(Authors) as cast return cast")
    List<Map<String, String>> smallWorldTheory(@Param("author1") String author1, @Param("author2") String author2);
    
//    @Query("MATCH p = (a:Author {name: {author1}})-[:PUBLISH*0..5]-(b:Author {name: {author2}})-[:CITE*0..5]-(a:Author {name: {author1}}) " +
//    "RETURN extract(n IN nodes(p)| coalesce(n.title,n.name)) AS `names AND titles`, length(p) ORDER BY length(p) LIMIT 10;")
    @Query("match (a:Author {name: {author1}}), (b:Author {name: {author2}}), p = shortestPath((a)-[*..10]-(b)) return extract(n IN nodes(p)| coalesce(n.title,n.name)) AS paper")
    List<Map<String, Object>> getAPath(@Param("author1") String author1, @Param("author2") String author2);
    
    @Query("MATCH (p:Paper) WHERE p.journal =~ ('(?i).*'+{journal}+'.*') AND p.year > {startYear} AND p.year < {endYear} RETURN p.title as paper, p.address as cast LIMIT 50")
    List<Map<String, String>> map_journal_yearRange(@Param("journal") String journal,@Param("startYear") Integer startYear, @Param("endYear") Integer endYear);
    
    @Query("MATCH (p:Paper)<-[:PUBLISH]-(a:Author) WHERE p.address = {location} AND p.title =~ ('(?i).*'+{keyWord1}+'.*') AND p.title =~ ('(?i).*'+{keyWord2}+'.*') RETURN a.name as author, p.address as cast LIMIT 50")
    List<Map<String, String>> map_location_keyWords(@Param("location") String location,@Param("keyWord1") String keyWord1, @Param("keyWord2") String keyWord2);

    
    @Query("MATCH (p:Paper)  WHERE p.title = {title} RETURN p.title as title, p.ee as ee, p.address as address, p.year as year, p.url as url, p.volume as volume, p.number as number, p.pages as pages, p.journal as journal, p.mdate as mdate,p.key as key ")
    List<Map<String, String>> getPaperDetails(@Param("title") String title);
    
}


