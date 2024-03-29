/*
 * $HeadURL$
 * $Author$
 * $Revision$
 * $Date$
 *
 * ====================================================================
 *
 * Copyright (C) 2008 by loveapple.cn
 *
 * All copyright notices regarding loveapple and loveapple CoreLib
 * MUST remain intact in the scripts, documents and source code.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public 
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 *
 * Correspondence and Marketing Questions can be sent to:
 * info at loveapple
 *
 * @author: loveapple
 */
package cn.loveapple.service.controller.pojo;

/**
 * 
 * 画面に表示するSelectボックスのアイテムを表すビーン
 * 
 * @author $Author$
 * @version $Revision$
 * @date $Date$
 * @id $Id$
 *
 */
public class FrontSelectItem {
	/**
	 * キーを表すコード値
	 */
	private String code;
	/**
	 * 表示するラベル
	 */
	private String label;
	/**
	 * キーを表すコード値を取得します。
	 * @return キーを表すコード値
	 */
	public String getCode() {
	    return code;
	}
	/**
	 * キーを表すコード値を設定します。
	 * @param code キーを表すコード値
	 */
	public void setCode(String code) {
	    this.code = code;
	}
	/**
	 * 表示するラベルを取得します。
	 * @return 表示するラベル
	 */
	public String getLabel() {
	    return label;
	}
	/**
	 * 表示するラベルを設定します。
	 * @param label 表示するラベル
	 */
	public void setLabel(String label) {
	    this.label = label;
	}
}
