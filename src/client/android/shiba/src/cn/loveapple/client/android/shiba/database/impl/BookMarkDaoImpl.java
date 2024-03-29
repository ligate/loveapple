/*
 * $HeadURL$
 * $Author$
 * $Revision$
 * $Date$
 *
 * ====================================================================
 *
 * Copyright (C) 2008 by loveapple.sourceforge.jp
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
package cn.loveapple.client.android.shiba.database.impl;

import static cn.loveapple.client.android.shiba.database.entity.BookMarkEntity.*;
import static cn.loveapple.client.android.Constant.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;
import cn.loveapple.client.android.LoveappleShibaDatabaseOpenHelper;
import cn.loveapple.client.android.shiba.database.BaseDao;
import cn.loveapple.client.android.shiba.database.BookMarkDao;
import cn.loveapple.client.android.shiba.database.entity.BookMarkEntity;
import cn.loveapple.client.android.util.StringUtils;

/**
 * 
 * @author loveapple
 * @version $Revision$
 * @date $Date$
 * @id $Id$
 *
 */
public class BookMarkDaoImpl extends BaseDao implements BookMarkDao {

	public BookMarkDaoImpl(LoveappleShibaDatabaseOpenHelper helper) {
		super(helper);
	}

	/**
	 * {@inheritDoc}
	 */
	public List<BookMarkEntity> getUrlHistory(String url, String title,
			int limit) {
		Cursor cursor = null;
		List<BookMarkEntity> result = null;
		StringBuilder condition = new StringBuilder();
		List<String> params = new ArrayList<String>();
		if(StringUtils.isNotEmpty(url)){
			condition.append(COLUMN_URL);
			condition.append(" LIKE '?%' ");
			params.add(url);
		}
		if(StringUtils.isNotEmpty(title)){
			if(StringUtils.isNotEmpty(url)){
				condition.append(" AND ");
			}
			condition.append(COLUMN_TITLE);
			condition.append(" LIKE '?%' ");
			params.add(title);
		}
		try{
			writableDb = getWritableDb();
			cursor = writableDb.query(
					TABLE_NAME, 
					null, 
					condition.toString() + " ORDER BY " + COLUMN_TIMESTAMP + " DESC" ,
					params.toArray(new String[]{}), null, null, null);
			result = new ArrayList<BookMarkEntity>();
			cursor.moveToFirst();
			while(!cursor.isAfterLast()){
				result.add(getUrlHistoryEntity(cursor));
				cursor.moveToNext();
			}
		}finally{
			if(cursor != null){
				cursor.close();
			}
		}
		return result;
	}
	
	/**
	 * 
	 * {@inheritDoc}
	 */
	public BookMarkEntity getUrlHistory(String url, String title) {
		List<BookMarkEntity> result = getUrlHistory(url, title, 1);
		if(result != null && 0 < result.size()){
			return result.get(0);
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public BookMarkEntity save(BookMarkEntity entity) {
		if(entity == null){
			throw new IllegalArgumentException("history entity is null.");
		}
		if(StringUtils.isEmpty(entity.getUrl())){
			throw new IllegalArgumentException("history url is null.");
		}

		ContentValues values = new ContentValues();
		values.put(COLUMN_URL, entity.getUrl());
		if(StringUtils.isNotEmpty(entity.getTitle())){
			values.put(COLUMN_TITLE, entity.getTitle());
		}
		if(entity.getTimestamp() != null){
			values.put(COLUMN_TIMESTAMP, entity.getTimestamp().getTime());
		}
		
		BookMarkEntity result = null;
		
		try{
			writableDb = getWritableDb();
			
			int colNum = writableDb.update(TABLE_NAME, values, COLUMN_URL + "=?", new String[]{entity.getUrl()});
			if(colNum < 1) {
				writableDb.insert(TABLE_NAME, null, values);
				Log.i(LOG_TAG, "update : " + ToStringBuilder.reflectionToString(values));
			}
			result = getUrlHistory(entity.getUrl(), null);
		}finally{
			//writableDb.close();
		}
		return result;
	}
	
	/**
	 * {@linkplain Cursor カーソル}から{@linkplain BookMarkEntity URL履歴}に変換する
	 * 
	 * @param cursor
	 * @return
	 */
	private BookMarkEntity getUrlHistoryEntity(Cursor cursor) {
		if(cursor == null || cursor.getCount() < 1){
			return null;
		}
		BookMarkEntity entity = new BookMarkEntity();
		entity.setUrl(cursor.getString(0));
		entity.setTitle(cursor.getString(1));
		entity.setTimestamp(new Date(cursor.getLong(2)));
		
		return entity;
	}

}
