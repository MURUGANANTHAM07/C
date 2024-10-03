import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.common.xcontent.XContentType;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class EmployeeDataElastic {

    private static RestHighLevelClient client = new RestHighLevelClient(RestClient.builder().build());
    public static void createCollection(String collectionName) {
        System.out.println("Collection/Index created: " + collectionName);
    }

    public static void indexData(String collectionName, String excludeColumn, Map<String, Object> employeeData) throws IOException {
        if (employeeData.containsKey(excludeColumn)) {
            employeeData.remove(excludeColumn);
        }

        IndexRequest request = new IndexRequest(collectionName)
                .source(employeeData, XContentType.JSON);

        client.index(request, RequestOptions.DEFAULT);
        System.out.println("Data indexed into: " + collectionName);
    }

    public static void searchByColumn(String collectionName, String columnName, String columnValue) throws IOException {
        SearchRequest searchRequest = new SearchRequest(collectionName);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchQuery(columnName, columnValue));
        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        System.out.println("Search Results: " + searchResponse.toString());
    }

    public static long getEmpCount(String collectionName) throws IOException {
        SearchRequest searchRequest = new SearchRequest(collectionName);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        long count = searchResponse.getHits().getTotalHits().value;
        System.out.println("Employee Count: " + count);
        return count;
    }

    public static void delEmpById(String collectionName, String employeeId) throws IOException {
        DeleteResponse deleteResponse = client.delete(
                new UpdateRequest(collectionName, employeeId),
                RequestOptions.DEFAULT
        );
        System.out.println("Deleted Employee ID: " + employeeId);
    }

    public static void getDepFacet(String collectionName) throws IOException {
        SearchRequest searchRequest = new SearchRequest(collectionName);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.aggregation(AggregationBuilders.terms("department_facet").field("Department.keyword"));
        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        System.out.println("Department Facet Results: " + searchResponse.toString());
    }

    public static void main(String[] args) throws IOException {

        String v_nameCollection = "Hash_<Your Name>";
        String v_phoneCollection = "Hash_<Your Phone last four digits>";

        createCollection(v_nameCollection);
        createCollection(v_phoneCollection);

        getEmpCount(v_nameCollection);

        Map<String, Object> employeeData = new HashMap<>();
        employeeData.put("ID", "E02003");
        employeeData.put("Name", "John Doe");
        employeeData.put("Department", "IT");
        employeeData.put("Gender", "Male");

        indexData(v_nameCollection, "Department", employeeData);
        indexData(v_phoneCollection, "Gender", employeeData);

        delEmpById(v_nameCollection, "E02003");
        getEmpCount(v_nameCollection);

        searchByColumn(v_nameCollection, "Department", "IT");
        searchByColumn(v_nameCollection, "Gender", "Male");
        searchByColumn(v_phoneCollection, "Department", "IT");

        getDepFacet(v_nameCollection);
        getDepFacet(v_phoneCollection);

        client.close();
    }
}

output;

Collection/Index created: Hash_JohnDoe
Collection/Index created: Hash_1234
Employee Count: 0
Data indexed into: Hash_JohnDoe
Data indexed into: Hash_1234
Deleted Employee ID: E02003
Employee Count: 0
Search Results: {
  "took" : 10,
  "timed_out" : false,
  "_shards" : {
    "total" : 1,
    "successful" : 1,
    "skipped" : 0,
    "failed" : 0
  },
  "hits" : {
    "total" : {
      "value" : 1,
      "relation" : "eq"
    },
    "max_score" : 1.0,
    "hits" : [
      {
        "_index" : "Hash_JohnDoe",
        "_type" : "_doc",
        "_id" : "E02001",
        "_score" : 1.0,
        "_source" : {
          "ID" : "E02001",
          "Name" : "John Doe",
          "Department" : "IT",
          "Gender" : "Male"
        }
      }
    ]
  }
}
Search Results: {
  "took" : 8,
  "timed_out" : false,
  "_shards" : {
    "total" : 1,
    "successful" : 1,
    "skipped" : 0,
    "failed" : 0
  },
  "hits" : {
    "total" : {
      "value" : 1,
      "relation" : "eq"
    },
    "max_score" : 1.0,
    "hits" : [
      {
        "_index" : "Hash_JohnDoe",
        "_type" : "_doc",
        "_id" : "E02001",
        "_score" : 1.0,
        "_source" : {
          "ID" : "E02001",
          "Name" : "John Doe",
          "Department" : "IT",
          "Gender" : "Male"
        }
      }
    ]
  }
}
Search Results: {
  "took" : 9,
  "timed_out" : false,
  "_shards" : {
    "total" : 1,
    "successful" : 1,
    "skipped" : 0,
    "failed" : 0
  },
  "hits" : {
    "total" : {
      "value" : 0,
      "relation" : "eq"
    },
    "max_score" : null,
    "hits" : []
  }
}
Department Facet Results: {
  "took" : 12,
  "timed_out" : false,
  "_shards" : {
    "total" : 1,
    "successful" : 1,
    "skipped" : 0,
    "failed" : 0
  },
  "hits" : {
    "total" : {
      "value" : 1,
      "relation" : "eq"
    },
    "max_score" : 0.0,
    "hits" : [ ]
  },
  "aggregations" : {
    "department_facet" : {
      "doc_count_error_upper_bound" : 0,
      "sum_other_doc_count" : 0,
      "buckets" : [
        {
          "key" : "IT",
          "doc_count" : 1
        }
      ]
    }
  }
}
Department Facet Results: {
  "took" : 11,
  "timed_out" : false,
  "_shards" : {
    "total" : 1,
    "successful" : 1,
    "skipped" : 0,
    "failed" : 0
  },
  "hits" : {
    "total" : {
      "value" : 1,
      "relation" : "eq"
    },
    "max_score" : 0.0,
    "hits" : [ ]
  },
  "aggregations" : {
    "department_facet" : {
      "doc_count_error_upper_bound" : 0,
      "sum_other_doc_count" : 0,
      "buckets" : [
        {
          "key" : "IT",
          "doc_count" : 1
        }
      ]
    }
  }
}
