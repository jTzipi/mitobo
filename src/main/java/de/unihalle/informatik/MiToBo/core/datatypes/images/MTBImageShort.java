/*
 * This file is part of MiToBo, the Microscope Image Analysis Toolbox.
 *
 * Copyright (C) 2010 - @YEAR@
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Fore more information on MiToBo, visit
 *
 *    http://www.informatik.uni-halle.de/mitobo/
 *
 */

/* 
 * Most recent change(s):
 * 
 * $Rev$
 * $Date$
 * $Author$
 * 
 */

package de.unihalle.informatik.MiToBo.core.datatypes.images;

import de.unihalle.informatik.Alida.admin.annotations.ALDMetaInfo;
import de.unihalle.informatik.Alida.admin.annotations.ALDMetaInfo.ExportPolicy;
import ij.ImagePlus;
import ij.gui.NewImage;

/**
 * Wrapper class for easy access to Short (hyper)stacks.
 * Voxel indices range is different from ImageJ
 * Here, indices in each dimension range from 0 to (dimSize - 1), while
 * ImageJ stack indices range from 1 to dimSize.
 * 
 * @author gress
 *
 */
@ALDMetaInfo(export=ExportPolicy.MANDATORY)
public class MTBImageShort extends MTBImage {

	/** reference to the ImagePlus pixel data */
	private short[][] m_data;
	
	/**
	 * Constructor
	 * @param img
	 */
	protected MTBImageShort(ImagePlus img) {
		super(img);
		
		// set image type
		m_type = MTBImageType.MTB_SHORT;
		
		// get data from underlying image
		m_data = new short[m_sizeStack][];
		
		for (int i = 1; i <= m_sizeStack; i++) {
			m_data[i-1] = (short[]) m_imgStack.getProcessor(i).getPixels();
		}
		
		// reference to the MTBImage from the ImagePlus object
		m_img.setProperty("MTBImage", this);
	}
	
	/**
	 * Constructor 
	 * @param sizeX size in x-dimension
	 * @param sizeY size in y-dimension
	 * @param sizeZ size in z-dimension
	 * @param sizeT size in t-dimension
	 * @param sizeC size in c-dimension
	 */
	protected MTBImageShort(int sizeX, int sizeY, int sizeZ, int sizeT, int sizeC) {
		super();
		
		// dimension sizes
		m_sizeX = sizeX;
		m_sizeY = sizeY;
		m_sizeZ = sizeZ;
		m_sizeT = sizeT;
		m_sizeC = sizeC;

	    this.setProperty("SizeX", m_sizeX);
	    this.setProperty("SizeY", m_sizeY);
	    this.setProperty("SizeZ", m_sizeZ);
	    this.setProperty("SizeT", m_sizeT);
	    this.setProperty("SizeC", m_sizeC);
	    
		m_sizeStack = m_sizeZ*m_sizeT*m_sizeC;
		
		// set image type
		m_type = MTBImageType.MTB_SHORT;
		
		// create new ImagePlus
		this.m_img = NewImage.createShortImage(this.getTitle(), 
				this.m_sizeX, this.m_sizeY, this.m_sizeStack, 
				NewImage.FILL_BLACK);
		m_img.setIgnoreFlush(true);
		m_imgStack = m_img.getStack();
		
		m_img.setDimensions(m_sizeC, m_sizeZ, m_sizeT);
		m_img.setOpenAsHyperStack((m_sizeC > 1) || (m_sizeT > 1));
		
		// get data from underlying image
		m_data = new short[m_sizeStack][];
		
		for (int i = 1; i <= m_sizeStack; i++) {
			m_data[i-1] = (short[]) m_imgStack.getProcessor(i).getPixels();
		}
		
		// reference to this MTBImage from the ImagePlus object
		m_img.setProperty("MTBImage", this);
	}

	/**
	 * Get an ImagePlus object. 
	 * A reference to the underlying ImagePlus is returned.
	 * @return ImagePlus object
	 */
	@Override
	public ImagePlus getImagePlus() {
		// synchronize titles between Mitobo image and ImagePlus
		this.m_img.setTitle(this.getTitle());
		return this.m_img;
	}
	
	/**
	 * Does nothing, because the underlying ImagePlus is updated immediately when values are changed in the MTBImage
	 */
	@Override
	protected void updateImagePlus() {
		// do nothing
	}
	
	/**
	 * Get the voxel value of the 5D image at coordinate (x,y,z,t,c)
	 * No test of coordinate validity
	 * @param x x-coordinate ranging from 0 to (sizeX - 1)
	 * @param y y-coordinate ranging from 0 to (sizeY - 1)
	 * @param z z-coordinate ranging from 0 to (sizeZ - 1)
	 * @param t t-coordinate ranging from 0 to (sizeT - 1)
	 * @param c c-coordinate ranging from 0 to (sizeC - 1)
	 * @return value as int
	 */
	@Override
	public int getValueInt(int x, int y, int z, int t, int c) {
				
        return (m_data[t*m_sizeC*m_sizeZ + z*m_sizeC + c][y*m_sizeX + x] & 0xffff);
	}

	/**
	 * Get the voxel value of the 5D image at coordinate (x,y,z,t,c)
	 * No test of coordinate validity
	 * @param x x-coordinate ranging from 0 to (sizeX - 1)
	 * @param y y-coordinate ranging from 0 to (sizeY - 1)
	 * @param z z-coordinate ranging from 0 to (sizeZ - 1)
	 * @param t t-coordinate ranging from 0 to (sizeT - 1)
	 * @param c c-coordinate ranging from 0 to (sizeC - 1)
	 * @return value as double
	 */
	@Override
	public double getValueDouble(int x, int y, int z, int t, int c) {
				
        return (double)(m_data[t*m_sizeC*m_sizeZ + z*m_sizeC + c][y*m_sizeX + x] & 0xffff);
	}
	
	/**
	 * Set the voxel value of the 5D image at coordinate (x,y,z,t,c)
	 * @param x x-coordinate ranging from 0 to (sizeX - 1)
	 * @param y y-coordinate ranging from 0 to (sizeY - 1)
	 * @param z z-coordinate ranging from 0 to (sizeZ - 1)
	 * @param t t-coordinate ranging from 0 to (sizeT - 1)
	 * @param c c-coordinate ranging from 0 to (sizeC - 1)
	 * @param value to set the voxel to 
	 */	
	@Override
	public void putValueInt(int x, int y, int z, int t, int c, int value) {
		
        m_data[t*m_sizeC*m_sizeZ + z*m_sizeC + c][y*m_sizeX + x] = (short)value;	
	}

	/**
	 * Set the voxel value of the 5D image at coordinate (x,y,z,t,c)
	 * @param x x-coordinate ranging from 0 to (sizeX - 1)
	 * @param y y-coordinate ranging from 0 to (sizeY - 1)
	 * @param z z-coordinate ranging from 0 to (sizeZ - 1)
	 * @param t t-coordinate ranging from 0 to (sizeT - 1)
	 * @param c c-coordinate ranging from 0 to (sizeC - 1)
	 * @param value to set the voxel to 
	 */	
	@Override
	public void putValueDouble(int x, int y, int z, int t, int c, double value) {
		
        m_data[t*m_sizeC*m_sizeZ + z*m_sizeC + c][y*m_sizeX + x] = (short)value;	
	}
	
	/**
	 * Get the voxel value of the actual z-stack at coordinate (x,y,z)
	 * No test of coordinate validity
	 * @param x x-coordinate ranging from 0 to (sizeX - 1)
	 * @param y y-coordinate ranging from 0 to (sizeY - 1)
	 * @param z z-coordinate ranging from 0 to (sizeZ - 1)
	 * @return value as int
	 */
	@Override
	public int getValueInt(int x, int y, int z) {
				
        return (m_data[m_currentT*m_sizeC*m_sizeZ + z*m_sizeC + m_currentC][y*m_sizeX + x] & 0xffff);
	}
	
	/**
	 * Get the voxel value of the actual z-stack at coordinate (x,y,z)
	 * No test of coordinate validity
	 * @param x x-coordinate ranging from 0 to (sizeX - 1)
	 * @param y y-coordinate ranging from 0 to (sizeY - 1)
	 * @param z z-coordinate ranging from 0 to (sizeZ - 1)
	 * @return value as int
	 */
	@Override
	public double getValueDouble(int x, int y, int z) {
				
        return (double)(m_data[m_currentT*m_sizeC*m_sizeZ + z*m_sizeC + m_currentC][y*m_sizeX + x] & 0xffff);
	}
	
	/**
	 * Set the voxel value of the actual z-stack at coordinate (x,y,z)
	 * @param x x-coordinate ranging from 0 to (sizeX - 1)
	 * @param y y-coordinate ranging from 0 to (sizeY - 1)
	 * @param z z-coordinate ranging from 0 to (sizeZ - 1)
	 * @param value to set the voxel to 
	 */	
	@Override
	public void putValueInt(int x, int y, int z, int value) {
		
        m_data[m_currentT*m_sizeC*m_sizeZ + z*m_sizeC + m_currentC][y*m_sizeX + x] = (short)value;	
	}
	
	/**
	 * Set the voxel value of the actual z-stack at coordinate (x,y,z)
	 * @param x x-coordinate ranging from 0 to (sizeX - 1)
	 * @param y y-coordinate ranging from 0 to (sizeY - 1)
	 * @param z z-coordinate ranging from 0 to (sizeZ - 1)
	 * @param value to set the voxel to 
	 */	
	@Override
	public void putValueDouble(int x, int y, int z, double value) {
		
        m_data[m_currentT*m_sizeC*m_sizeZ + z*m_sizeC + m_currentC][y*m_sizeX + x] = (short)value;	
	}
	
	/**
	 * Get the value of the actual slice at coordinate (x,y) as an Integer
	 * @param x x-coordinate ranging from 0 to (sizeX - 1)
	 * @param y y-coordinate ranging from 0 to (sizeY - 1)
	 * @return voxel value
	 */
	@Override
	public int getValueInt(int x, int y) {
        return (m_data[m_currentSliceIdx][y*m_sizeX + x] & 0xffff);
	}
	
	/**
	 * Get the value of the actual slice at coordinate (x,y) as an Double
	 * @param x x-coordinate ranging from 0 to (sizeX - 1)
	 * @param y y-coordinate ranging from 0 to (sizeY - 1)
	 * @return voxel value
	 */
	@Override
	public double getValueDouble(int x, int y) {
		return (double)(m_data[m_currentSliceIdx][y*m_sizeX + x] & 0xffff);
	}
	
	/**
	 * Set the value of the actual slice at coordinate (x,y) using an Integer
	 * @param x x-coordinate ranging from 0 to (sizeX - 1)
	 * @param y y-coordinate ranging from 0 to (sizeY - 1)
	 * @param value to set the voxel to 
	 */	
	@Override
	public void putValueInt(int x, int y, int value) {
		m_data[m_currentSliceIdx][y*m_sizeX + x] = (short)value;	
	}
	
	/**
	 * Set the value of the actual slice at coordinate (x,y) using a Double
	 * @param x x-coordinate ranging from 0 to (sizeX - 1)
	 * @param y y-coordinate ranging from 0 to (sizeY - 1)
	 * @param value to set the voxel to 
	 */	
	@Override
	public void putValueDouble(int x, int y, double value) {
		m_data[m_currentSliceIdx][y*m_sizeX + x] = (short)value;
	}
	
	/**
	 * Get minimum and maximum value of the image as int
	 * @return min at int[0], max at int[1]
	 */
	@Override
	public int[] getMinMaxInt() {
		int[] minmax = null;
		int sizeXY = m_sizeX*m_sizeY;
		int val;
		
		for (int i = 0; i < m_sizeStack; i++) {
			for (int j = 0; j < sizeXY; j++) {
				
				val = m_data[i][j] & 0xffff;
				
				if (minmax == null) {
					minmax = new int[2];
					minmax[0] = val;
					minmax[1] = val;
				}
				else {
					
					if (val < minmax[0])
						minmax[0] = val;
					
					if (val > minmax[1])
						minmax[1] = val;
				}		
			}
		}
		
		return minmax;
	}
	
	/**
	 * Get minimum and maximum value of the image as double
	 * @return min at double[0], max at double[1]
	 */
	@Override
	public double[] getMinMaxDouble() {
		double[] minmax = null;
		int sizeXY = m_sizeX*m_sizeY;
		double val;
		
		for (int i = 0; i < m_sizeStack; i++) {
			for (int j = 0; j < sizeXY; j++) {
				
				val = m_data[i][j] & 0xffff;
				
				if (minmax == null) {
					minmax = new double[2];
					minmax[0] = val;
					minmax[1] = val;
				}
				else {
					
					if (val < minmax[0])
						minmax[0] = val;
					
					if (val > minmax[1])
						minmax[1] = val;
				}		
			}
		}
		
		return minmax;
	}
	
}
