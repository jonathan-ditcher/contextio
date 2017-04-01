package com.ditcherj.contextio.responses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Author: Jonathan Ditcher
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ListContactsResponse extends BaseResponse {

	private Query query;
	private List<Match> matches;

	public ListContactsResponse() {
	}

	public List<Match> getMatches() {
		return matches;
	}

	public void setMatches(List<Match> matches) {
		this.matches = matches;
	}

	public Query getQuery() {
		return query;
	}

	public void setQuery(Query query) {
		this.query = query;
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Match{

		private String email;
		private Integer count;
		private String name;
		private String thumbnail;

		public Match() {
		}

		public Integer getCount() {
			return count;
		}

		public void setCount(Integer count) {
			this.count = count;
		}

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getThumbnail() {
			return thumbnail;
		}

		public void setThumbnail(String thumbnail) {
			this.thumbnail = thumbnail;
		}

		@Override
		public String toString() {
			return "Match{" +
					"count=" + count +
					", email='" + email + '\'' +
					", name='" + name + '\'' +
					", thumbnail='" + thumbnail + '\'' +
					'}';
		}
	}

	public static class Query{
		
		private Integer limit;
		private Integer offset;
		private Integer active_after;
		private Integer active_before;
		private String search;

		public Query() {
		}

		public Integer getActive_after() {
			return active_after;
		}

		public void setActive_after(Integer active_after) {
			this.active_after = active_after;
		}

		public Integer getActive_before() {
			return active_before;
		}

		public void setActive_before(Integer active_before) {
			this.active_before = active_before;
		}

		public Integer getLimit() {
			return limit;
		}

		public void setLimit(Integer limit) {
			this.limit = limit;
		}

		public Integer getOffset() {
			return offset;
		}

		public void setOffset(Integer offset) {
			this.offset = offset;
		}

		public String getSearch() {
			return search;
		}

		public void setSearch(String search) {
			this.search = search;
		}

		@Override
		public String toString() {
			return "Query{" +
					"active_after=" + active_after +
					", limit=" + limit +
					", offset=" + offset +
					", active_before=" + active_before +
					", search='" + search + '\'' +
					'}';
		}
	}

    @Override
    public String toString() {
        return "ListContactsResponse{" +
                "query=" + query +
                ", matches=" + matches +
                "} " + super.toString();
    }
}
