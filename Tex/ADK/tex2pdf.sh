#!/bin/bash

  if [ -e  $1.tex ]; then
    TEX_DATE=$(date +%Y%m%d);
    if [ -e $TEX_DATE ]; then
      echo overwrite!!!
    else
      mkdir $TEX_DATE;
    fi
    cp $1.tex $TEX_DATE.tex;
    nkf -w --overwrite $TEX_DATE.tex;
    platex $TEX_DATE;
    pbibtex $TEX_DATE;
    platex $TEX_DATE;
    platex $TEX_DATE;
    dvipdfmx $TEX_DATE.dvi;
    mv $TEX_DATE.* $TEX_DATE;
    evince $TEX_DATE/$TEX_DATE.pdf;
  else
    echo usage : tex2pdf sample;
    echo wrong_case : tex2pdg sample.tex;
  fi
