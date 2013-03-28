package org.dream.dreambox.common.component.lucene;

import java.io.Serializable;
import java.util.List;

public class QueryResult implements Serializable
{
	private static final long serialVersionUID = 5836948853695614883L;
	
	private int recordCount = 0;
	private List<QueryResultItem> recordList = null;
	
	public QueryResult(int recordCount, List<QueryResultItem> recordList)
	{
		super();
		this.recordCount = recordCount;
		this.recordList = recordList;
	}
	
	public int getRecordCount()
	{
		return recordCount;
	}

	public void setRecordCount(int recordCount)
	{
		this.recordCount = recordCount;
	}

	public List<QueryResultItem> getRecordList()
	{
		return recordList;
	}

	public void setRecordList(List<QueryResultItem> recordList)
	{
		this.recordList = recordList;
	}
}
