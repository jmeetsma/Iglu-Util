/*
 * Copyright 2011-2014 Jeroen Meetsma - IJsberg Automatisering BV
 *
 * This file is part of Iglu.
 *
 * Iglu is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Iglu is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Iglu.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.ijsberg.iglu.util.mail;

import java.util.HashMap;

/**
 * Helper class containing current mime types.
 */
public abstract class MimeTypeSupport {
	public static HashMap<String, String> mapping = new HashMap<String, String>(10);
	//static initialization
	public static FileExtensionToTypeMapping fe2tmapping = new FileExtensionToTypeMapping();

	public static class FileExtensionToTypeMapping {
		private FileExtensionToTypeMapping() {
			mapping.put("html", "text/html");
			mapping.put("htm", "text-html");
			mapping.put("txt", "text/plain");
			mapping.put("c", "text/plain");
			mapping.put("c++", "text/plain");
            mapping.put("cpp", "text/plain");
			mapping.put("pl", "text/plain");
			mapping.put("cc", "text/plain");
			mapping.put("h", "text/plain");
			mapping.put("talk", "text/x-speech");
			mapping.put("css", "text/css");
			mapping.put("gif", "image/gif");
			mapping.put("xbm", "image/x-xbitmap");
			mapping.put("xpm", "image/x-xpixmap");
			mapping.put("png", "image/x-png");
			mapping.put("ief", "image/ief");
			mapping.put("jpeg", "image/jpeg");
			mapping.put("jpg", "image/jpeg");
			mapping.put("jpe", "image/jpeg");
			mapping.put("tiff", "image/tiff");
			mapping.put("tif", "image/tiff");
			mapping.put("rgb", "image/rgb");
			mapping.put("g3f", "image/g3fax");
			mapping.put("xwd", "image/x-xwindowdump");
			mapping.put("pict", "image/x-pict");
			mapping.put("ppm", "image/x-portable-pixmap");
			mapping.put("pgm", "image/x-portable-graymap");
			mapping.put("pbm", "image/x-portable-bitmap");
			mapping.put("pnm", "image/x-portable-anymap");
			mapping.put("bmp", "image/x-ms-bmp");
			mapping.put("ras", "image/x-cmu-raster");
			mapping.put("pcd", "image/x-photo-cd");
			mapping.put("cgm", "image/cgm");
			mapping.put("mil", "image/x-cals");
			mapping.put("cal", "image/x-cals");
			mapping.put("fif", "image/fif");
			mapping.put("dsf", "image/x-mgx-dsf");
			mapping.put("cmx", "image/x-cmx");
			mapping.put("wi", "image/wavelet");
			mapping.put("dwg", "image/vnd.dwg");
			mapping.put("dxf", "image/vnd.dxf");
			mapping.put("svf", "image/vnd.svf");
			mapping.put("au", "audio/basic");
			mapping.put("snd", "audio/basic");
			mapping.put("aif", "audio/x-aiff");
			mapping.put("aiff", "audio/x-aiff");
			mapping.put("aifc", "audio/x-aiff");
			mapping.put("wav", "audio/x-wav");
			mapping.put("mpa", "audio/x-mpeg");
			mapping.put("abs", "audio/x-mpeg");
			mapping.put("mpega", "audio/x-mpeg");
			mapping.put("mp2a", "audio/x-mpeg-2");
			mapping.put("mpa2", "audio/x-mpeg-2");
			mapping.put("es", "audio/echospeech");
			mapping.put("vox", "audio/voxware");
			mapping.put("lcc", "application/fastman");
			mapping.put("ra", "application/x-pn-realaudio");
			mapping.put("ram", "application/x-pn-realaudio");
			mapping.put("mmid", "x-music/x-midi");
			mapping.put("skp", "application/vnd.koan");
			mapping.put("talk", "text/x-speech");
			mapping.put("mpeg", "video/mpeg");
			mapping.put("mpg", "video/mpeg");
			mapping.put("mpe", "video/mpeg");
			mapping.put("mpv2", "video/mpeg-2");
			mapping.put("mp2v", "video/mpeg-2");
			mapping.put("qtmov", "video/quicktime");
			mapping.put("avi", "video/x-msvideo");
			mapping.put("movie", "video/x-sgi-movie");
			mapping.put("vdo", "video/vdo");
			mapping.put("viv", "video/vnd.vivo");
			mapping.put("pac", "application/x-ns-proxy-autoconfig");
			mapping.put("ice", "x-conference/x-cooltalk");
			mapping.put("ai", "application/postscript");
			mapping.put("eps", "application/postscript");
			mapping.put("ps", "application/postscript");
			mapping.put("rtf", "application/rtf");
			mapping.put("pdf", "application/pdf");
			mapping.put("mif", "application/vnd.mif");
			mapping.put("t", "application/x-troff");
			mapping.put("tr", "application/x-troff");
			mapping.put("troff", "application/x-troff");
			mapping.put("man", "application/x-troff-man");
			mapping.put("me", "application/x-troff-me");
			mapping.put("ms", "application/x-troff-ms");
			mapping.put("latex", "application/x-latex");
			mapping.put("tex", "application/x-tex");
			mapping.put("texinfo", "application/x-texinfo");
			mapping.put("texi", "application/x-texinfo");
			mapping.put("dvi", "application/x-dvi");
			mapping.put("oda", "application/oda");
			mapping.put("evy", "application/envoy");
			mapping.put("doc", "application/vnd.framemaker");
			mapping.put("fm", "application/vnd.framemaker");
			mapping.put("frm", "application/vnd.framemaker");
			mapping.put("frame", "application/vnd.framemaker");
			mapping.put("gtar", "application/x-gtar");
			mapping.put("tar", "application/x-tar");
			mapping.put("ustar", "application/x-ustar");
			mapping.put("bcpio", "application/x-bcpio");
			mapping.put("cpio", "application/x-cpio");
			mapping.put("shar", "application/x-shar");
			mapping.put("zip", "application/zip");
			mapping.put("hqx", "application/mac-binhex40");
			mapping.put("sit", "application/x-stuffit");
			mapping.put("sea", "application/x-stuffit");
			mapping.put("fif", "application/fractals");
			mapping.put("bin", "application/octet-stream");
			mapping.put("uu", "application/octet-stream");
			mapping.put("exe", "application/octet-stream");
			mapping.put("src", "application/x-wais-source");
			mapping.put("wsrc", "application/x-wais-source");
			mapping.put("hdf", "application/hdf");
			mapping.put("js", "text/javascript");
			mapping.put("ls", "application/x-javascript");
			mapping.put("mocha", "text/vbscript");
			mapping.put("sh", "application/x-sh");
			mapping.put("csh", "application/x-csh");
			mapping.put("pl", "application/x-perl");
			mapping.put("tcl", "application/x-tcl");
			mapping.put("spl", "application/futuresplash");
			mapping.put("mbd", "application/mbedlet");
			mapping.put("rad", "application/x-rad-powermedia");
			mapping.put("ppz", "application/mspowerpoint");
			//W3C specifies "text/css" (http://www.w3.org/TR/CSS2/conform.html)
			//mapping.put("css","application/x-pointplus");
			mapping.put("asp", "application/x-asap");
			mapping.put("asn", "application/astound");
			mapping.put("axs", "application/x-olescript");
			mapping.put("ods", "application/x-oleobject");
			mapping.put("opp", "x-form/x-openscape");
			mapping.put("wba", "application/x-webbasic");
			mapping.put("frm", "application/x-alpha-form");
			mapping.put("wfx", "x-script/x-wfxclient");
			mapping.put("pcn", "application/x-pcn");
			mapping.put("ppt", "application/vnd.ms-powerpoint");
			mapping.put("svd", "application/vnd.svd");
			mapping.put("ins", "application/x-net-install");
			mapping.put("ccv", "application/ccv");
			mapping.put("vts", "workbook/formulaone");
			mapping.put("wrl", "x-world/x-vrml");
			mapping.put("vrml", "x-world/x-vrml");
			mapping.put("vrw", "x-world/x-vream");
			mapping.put("p3d", "application/x-p3d");
			mapping.put("svr", "x-world/x-svr");
			mapping.put("wvr", "x-world/x-wvr");
			mapping.put("3dmf", "x-world/x-3dmf");
			mapping.put("ma", "application/mathematica");
			mapping.put("msh", "x-model/x-mesh");
			mapping.put("v5d", "application/vis5d");
			mapping.put("igs", "application/iges");
			mapping.put("dwf", "drawing/x-dwf");
			mapping.put("showcase", "application/x-showcase");
			mapping.put("slides", "application/x-showcase");
			mapping.put("sc", "application/x-showcase");
			mapping.put("sho", "application/x-showcase");
			mapping.put("show", "application/x-showcase");
			mapping.put("ins", "application/x-insight");
			mapping.put("insight", "application/x-insight");
			mapping.put("ano", "application/x-annotator");
			mapping.put("dir", "application/x-dirview");
			mapping.put("lic", "application/x-enterlicense");
			mapping.put("faxmgr", "application/x-fax-manager");
			mapping.put("faxmgrjob", "application/x-fax-manager-job");
			mapping.put("icnbk", "application/x-iconbook");
			mapping.put("wb", "application/x-inpview");
			mapping.put("inst", "application/x-install");
			mapping.put("mail", "application/x-mailfolder");
			mapping.put("pp", "application/x-ppages");
			mapping.put("ppages", "application/x-ppages");
			mapping.put("sgi-lpr", "application/x-sgi-lpr");
			mapping.put("tardist", "application/x-tardist");
			mapping.put("ztardist", "application/x-ztardist");
			mapping.put("wkz", "application/x-wingz");
			mapping.put("iv", "graphics/x-inventor");
		}
	}

	/**
	 * @param extension
	 * @return
	 */
	public static String getMimeTypeForFileExtension(String extension) {
		String retval = mapping.get(extension);
        if (retval == null) {
            retval = "text/plain";
        }
        return retval;
	}
}
