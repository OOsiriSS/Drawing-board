package com.nju.DataDservice;

import com.nju.Domain.Paint;


public interface PaintDataService {
    boolean savePaintAsTxt(Paint paint);
    Paint getSavedPaint();
}
