package bean;

import java.util.ArrayList;
import java.util.List;

public class FirstTenCommentNumberAYear {
	private Integer year = 0;
	private Integer totalCommentNumber = 0;
	private List<UsernameCommentnumber> firstTenCommentNumberList = new ArrayList<UsernameCommentnumber>();

	public FirstTenCommentNumberAYear() {
		for (int i = 0; i < 10; i++) {
			UsernameCommentnumber usernameCommentnumber = new UsernameCommentnumber();
			firstTenCommentNumberList.add(usernameCommentnumber);
		}
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public Integer getTotalCommentNumber() {
		return totalCommentNumber;
	}

	public void setTotalCommentNumber(Integer totalCommentNumber) {
		this.totalCommentNumber = totalCommentNumber;
	}

	public List<UsernameCommentnumber> getFirstTenCommentNumberList() {
		return firstTenCommentNumberList;
	}

	public void setFirstTenCommentNumberList(List<UsernameCommentnumber> firstTenCommentNumberList) {
		this.firstTenCommentNumberList = firstTenCommentNumberList;
	}

}
