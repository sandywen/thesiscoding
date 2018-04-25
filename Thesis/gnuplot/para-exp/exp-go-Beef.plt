set output "/Users/wencheng/thesis/WenCheng/thuthesis/figures/exp-go-Beef.eps"
set key inside right bottom
set title "Beef" font "Verdana,25" 
set ylabel ""
set auto y
set xlabel "go" font "Verdana,25"
unset logscale y
set ytics font "Verdana,18"
set xtics font "Verdana,18"
set style data linespoints
set style fill pattern 1 border -1
plot '/Users/wencheng/thesiscoding/Thesis/experiments/exp-co-latest-Beef.dat' using 2:xticlabels(1) title column(2) w lp lw 4 pt 5 ps 3, \
		           '' using 3:xticlabels(1) title column(3) w lp lw 4 pt 4 ps 3,\
				   '' using 4:xticlabels(1) title column(4) w lp lw 4 pt 3 ps 3