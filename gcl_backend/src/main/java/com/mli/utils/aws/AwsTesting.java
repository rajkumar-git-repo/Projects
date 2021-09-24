package com.mli.utils.aws;

import org.springframework.beans.factory.annotation.Value;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;

public class AwsTesting {

	private String awsS3Bucket="maxlifedev";

	private String accessKeyId="AKIARONY63T5X6GTA3NS";

	private String secretAccessKey="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";

	public void copyFolder(String source, String destination) {
		AWSCredentials credentials = new BasicAWSCredentials(accessKeyId, secretAccessKey);
		AmazonS3 s3client = new AmazonS3Client(credentials);
		ObjectListing objects = s3client
				.listObjects(new ListObjectsRequest().withBucketName(awsS3Bucket).withPrefix(source));

		for (S3ObjectSummary objectSummary : objects.getObjectSummaries()) {
			System.out.println(objectSummary.toString());
			
			s3client.copyObject(this.awsS3Bucket, objectSummary.getKey(), this.awsS3Bucket, destination+objectSummary.getKey());
		
		}
	}

	public static void main(String[] args) {
		AwsTesting awsTesting=new AwsTesting();
		awsTesting.copyFolder("CAM_REPORT/P0409201800004", "verfied_27-08-2019/");
	}

}
