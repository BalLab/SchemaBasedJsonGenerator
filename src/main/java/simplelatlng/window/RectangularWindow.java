/*
 *    Copyright 2010 Tyler Coles
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package simplelatlng.window;

import static java.lang.Math.abs;
import static java.lang.Math.min;
import simplelatlng.LatLng;
import simplelatlng.LatLngTool;

/**
 * <p>A "pseudo-rectangular" window bounded by a minimum and maximum latitude
 * and a minimum and maximum longitude. (Large windows will lose the 
 * approximately rectangular shape.) Naturally a window cannot span more than 180 
 * degrees latitude or 360 degrees longitude.</p>
 * 
 * <p>Note: the latitude span provided when creating this window is not a guarantee. 
 * If you create a latitude whose center is (90, 0) (the geographic North Pole) and 
 * whose latitude span is 10 degrees, the resulting window has a maximum latitude of 90 
 * and a minimum latitude of 85. Thus, windows are "squashed" if they hit the poles.</p>
 * 
 * @author Tyler Coles
 */
public class RectangularWindow implements LatLngWindow {

	// TODO: get width and height (in length units and degrees) methods 

	private double minLatitude;
	private double maxLatitude;
	private double minLongitude;
	private double maxLongitude;
	private boolean crosses180thMeridian;
	private LatLng center;

	/**
	 * Creates a pseudo-rectangular window.
	 * 
	 * @param center the center point.
	 * @param deltaLat the span of the window in latitude in degrees.
	 * @param deltaLng the span of the window in longitude in degrees.
	 */
	public RectangularWindow(LatLng center, double deltaLat, double deltaLng) {
		this.setWindow(center, deltaLat, deltaLng);
	}

	/**
	 * Sets the bounds of this window.
	 * 
	 * @param center the center point.
	 * @param deltaLat the span of the window in latitude in degrees.
	 * @param deltaLng the span of the window in longitude in degrees.
	 */
	public void setWindow(LatLng center, double deltaLat, double deltaLng) {
		if (deltaLat == Double.NaN || deltaLat == Double.POSITIVE_INFINITY
				|| deltaLat == Double.NEGATIVE_INFINITY)
			throw new IllegalArgumentException("Invalid latitude delta.");
		if (deltaLng == Double.NaN || deltaLng == Double.POSITIVE_INFINITY
				|| deltaLng == Double.NEGATIVE_INFINITY)
			throw new IllegalArgumentException("Invalid longitude delta.");
		double dlat = min(abs(deltaLat), 180.0);
		this.setLatWindow(center.getLatitude(), dlat);

		double dlng = min(abs(deltaLng), 360.0);
		this.setLngWindow(center.getLongitude(), dlng);

		this.center = center;
	}

	/**
	 * Fixes and sets the latitude parameters for the window.
	 */
	private void setLatWindow(double centerLat, double deltaLat) {
		double lat1 = LatLngTool.normalizeLatitude(centerLat + (deltaLat / 2.0));
		double lat2 = LatLngTool.normalizeLatitude(centerLat - (deltaLat / 2.0));
		this.maxLatitude = Math.max(lat1, lat2);
		this.minLatitude = Math.min(lat1, lat2);
	}

	/**
	 * Fixes and sets the longitude parameters for the window.
	 */
	private void setLngWindow(double centerLng, double deltaLng) {
		double lng1 = centerLng + (deltaLng / 2.0);
		double lng2 = centerLng - (deltaLng / 2.0);
		if (lng1 > 180 || lng2 < -180) {
			this.crosses180thMeridian = true;
		} else {
			this.crosses180thMeridian = false;
		}
		lng1 = LatLngTool.normalizeLongitude(lng1);
		lng2 = LatLngTool.normalizeLongitude(lng2);
		this.maxLongitude = Math.max(lng1, lng2);
		this.minLongitude = Math.min(lng1, lng2);
	}

	// @Override
	public boolean contains(LatLng point) {

		boolean onLatLine = LatLng.degreesEqual(point.getLatitude(), maxLatitude)
				|| LatLng.degreesEqual(point.getLatitude(), minLatitude);
		if (!onLatLine
				&& (point.getLatitude() > maxLatitude || point.getLatitude() < minLatitude)) {
			return false;
		}

		if (LatLng.degreesEqual(point.getLongitude(), maxLongitude)
				|| LatLng.degreesEqual(point.getLongitude(), minLongitude)) {
			// Passed latitude test, lies on a longitude line.
			return true;
		}

		if (crosses180thMeridian) {
			if (point.getLongitude() < maxLongitude
					&& point.getLongitude() > minLongitude) {
				return false;
			}
		} else {
			if (point.getLongitude() > maxLongitude
					|| point.getLongitude() < minLongitude) {
				return false;
			}
		}
		return true;
	}

	// @Override
	public LatLng getCenter() {
		return center;
	}

	/**
	 * If this window spans the 180 degree longitude meridian, this method
	 * returns true. Logic that uses this window in calculations may need
	 * to handle it specially. In this case, minLatitude is the negative-degree 
	 * meridian and maxLatitude is the positive-degree meridian and the
	 * window should extend from both lines to the 180 degree meridian.
	 * So instead of testing whether a point lies between the min/max-longitude,
	 * you would have to test if a point lay outside the min/max-longitude.
	 * 
	 * @return true if this window spans the 180th meridian. 
	 */
	public boolean crosses180thMeridian() {
		return crosses180thMeridian;
	}

	public double getMinLatitude() {
		return minLatitude;
	}

	public double getMaxLatitude() {
		return maxLatitude;
	}

	public double getMinLongitude() {
		return minLongitude;
	}

	public double getMaxLongitude() {
		return maxLongitude;
	}
}
