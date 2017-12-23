package com.pear.yellowthird.vo;

import java.util.List;

/**
 * 一个目录的相册对象
 * 
 * @author Administrator
 * 
 */
public class ImageBucket {

	/**相册下总张数*/
	public int count = 0;

	/**目录名称*/
	public String bucketName;

	/**当前目录下的图片集合*/
	public List<ImageItem> imageList;

}
