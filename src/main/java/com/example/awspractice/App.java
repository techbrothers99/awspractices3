package com.example.awspractice;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.DeleteBucketRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadBucketRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

/**
 * Hello world!
 *
 */
public class App 
{
	public static void main( String[] args )    {
        System.out.println( "Hello World!" );
        
        Region region = Region.US_EAST_1;
        S3Client s3Client = S3Client.builder().region(region).build();
        
        String bucketName = "bucket" + System.currentTimeMillis();
        String key = "key";
        
        tutorialSetup(region, s3Client, bucketName);
        
        System.out.println("Uploading Objects");
        
        s3Client.putObject(PutObjectRequest.builder().bucket(bucketName).key(key)
        		.build(), RequestBody.fromString("Testing with the {sdk-java}"));
        
        System.out.println("Upload complete");
        System.out.println("%n");
        
        cleanUp(s3Client, bucketName, key);
        
        System.out.println("Closing the connection to {S3}");
        s3Client.close();
        System.out.println("Connection closed");
        System.out.println("Exiting...");
        
        
    }

	private static void tutorialSetup(Region region, S3Client s3Client, String bucketName) {
		try {
			s3Client.createBucket(CreateBucketRequest
	        		.builder()
	        		.bucket(bucketName).build());
	        System.out.println("Creating a bucket: "+ bucketName);
	        s3Client.waiter().waitUntilBucketExists(HeadBucketRequest.builder()
	        		.bucket(bucketName)
	        		.build());
	        System.out.println(bucketName + " is ready");
	        System.out.println("%n");
		} catch(S3Exception e) {
			System.err.println(e.awsErrorDetails().errorMessage());
		}
	}
	
	private static void cleanUp(S3Client s3Client, String bucketName, String key) {
		System.out.println("Cleaning up...");
		try {
			System.out.println("Deleting object: " + key);
	        DeleteObjectRequest deleteObjectRequest= DeleteObjectRequest.builder().bucket(bucketName).key(key).build();
	        s3Client.deleteObject(deleteObjectRequest);
	        System.out.println(key + "has been deleted");
	        System.out.println( "Deleting bucket: "+ bucketName);
	        DeleteBucketRequest deleteBucketRequest = DeleteBucketRequest.builder().bucket(bucketName).build();
	        s3Client.deleteBucket(deleteBucketRequest);
	        System.out.println(bucketName + " has been deleted");
	        System.out.println("%n");
		} catch (S3Exception e) {
		      System.err.println(e.awsErrorDetails().errorMessage());
		      System.exit(1);
		}
		System.out.println("Cleanup complete");
	    System.out.printf("%n");
        
	}
}
