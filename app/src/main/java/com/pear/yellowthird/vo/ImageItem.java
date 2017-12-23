package com.pear.yellowthird.vo;

import java.io.Serializable;

/**
 * 一个图片对象
 * 
 * @author Administrator
 * 
 */
public class ImageItem implements Serializable {

	/**图片ID*/
	public String imageId;

	/**略缩图的路径*/
	public String thumbnailPath;

	/**图片的源地址*/
	public String imagePath;

	/**是否选中了该图片*/
	public boolean isSelected = false;

}
