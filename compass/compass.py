# -*- coding: utf-8 -*-
"""
Created on Sat Mar 12 13:08:39 2022

@author: joelb
"""

import math
import imageio
import numpy as np
import cmath

def rotate(img1,img2,azimuth,pitch,roll):
    out=[]
    for i in range(len(img1)):
        lne=[]
        for j in range(len(img1[i])):
            x=i-len(img1)/2
            y=j-len(img1[i])/2
            X=x/math.cos(pitch*math.pi/180)
            Y=y/math.cos(roll*math.pi/180)-(x*math.sin(pitch*math.pi/180)*math.sin(roll*math.pi/180))/(math.cos(roll*math.pi/180)*math.cos(pitch*math.pi/180))
            k=X+1j*Y
            k*=cmath.exp(1j*azimuth*math.pi/180)
            X,Y=k.real,k.imag
            X+=len(img1)/2
            Y+=len(img1[0])/2
            if(X<0 or X>=len(img1) or Y<0 or Y>=len(img1[0])):
                lne.append([255,255,255])
            else:
                if(math.cos(math.pi*pitch/180)>0):
                    lne.append(img1[int(X)][int(Y)])
                else:
                    lne.append(img2[int(X)][int(Y)])
        out.append(lne)
    return out


def disp(k):
    if(k>0):
        return 'p'+str(k)
    else:
        return "m"+str(abs(k))          
              

img1=imageio.imread('comp1.bmp')
img2=imageio.imread('comp2.bmp')

for azimuth in range(0,390,30):
    for pitch in range(-180,210,30):
        for roll in range(-90,120,30):
            out=rotate(img1,img2,azimuth,pitch,roll)
            imageio.imwrite('rotated_compass_'+disp(azimuth)+'_'+disp(pitch)+'_'+disp(roll)+'.png',np.array(out,dtype='uint8'))
    