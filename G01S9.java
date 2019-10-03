
// EPanel NCNU-LAB-114 LWJ 2014-07 -> 2015-08
//標頭檔import
import java.io.*;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.Timer;
import javax.swing.event.*;
import javax.swing.border.*;

import java.text.*;
import java.util.*;

import javax.sound.sampled.*;

import java.net.*;
//開啟舊檔
import java.lang.*;

import javax.swing.filechooser.*;

import java.awt.image.BufferedImage;
import java.net.URL;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.io.File;
import java.util.List;

import javax.imageio.*;

import ch.randelshofer.media.avi.AVIOutputStream;

import javax.imageio.ImageIO;

//結構Particle
class Particle_Initial {
	int Amount; // 粒子bit數
	double Fitness; // 粒子Fitness
	String PInfo; // 粒子資訊

	int Bit[]; // 各bit值
	float Probability[]; // 各bit機率
	String BitInfo[]; // 各bit資訊

	public Particle_Initial() {
		super();
	}
}

//結構Generation
class Generation_Initial {
	int Amount; // 當代檔案數量
	int Particle; // 當代粒子數
	double GBest; // 目前最佳解
	int GBest_gen; // 目前最佳解的代數
	int CBest; // 當代最佳解位置
	int Evaluation;

	Particle_Initial X[];

	// 初始化
	public Generation_Initial() {
		super();
	}
}

class Map_Initial {
	int row;
	int col;
	int Count;
	double limit[][];
	int Sensor[][];
	double Probability[][];
}

//主程式main
public class G01S1 {
	public static void main(String[] args) throws Exception {
		// 更改UI介面 成Windows
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			// SwingUtilities.updateComponentTreeUI(chooser);
		} catch (Exception try_e) {
			try_e.printStackTrace();
		}
		// 宣告視窗
		Show frame_Show = new Show(); // 介面
		Show.frame_Player = new Player(); // 播放器
		Show.frame_Converse = new Converse();
		frame_Show.setVisible(true);
		Show.frame_Player.setVisible(false);
		Show.frame_Converse.setVisible(false);
	}
}

//Show 介面class 主要的顯示畫面
class Show extends EPanel implements ActionListener {

	// erace_tabulist
	double[] tabulist;

	// ---------------------------主要設定區
	// 版本號
	static String Version = "G01S9";
	// 粒子顯示數量
	static int Particle_shownum = 20;
	// 設定數字格式
	// ????//
	DecimalFormat df = new DecimalFormat("###0.0");
	// 播放器視窗 
	static Player frame_Player;
	static Converse frame_Converse;

	// 上限設定
	// why??? array
	int Image_T_MAX = 44; // 功能圖片數量
	static int Point_MAX = 10000; // 粒子數量
	static int Generation_MAX = 400000; // 世代數量
	static int Fitness_JLabel_MAX = 10; // 左下灰框框

	// ---------------------------視窗物件區
	// ----------上方灰區域
	JPanel Header_JPanel;

	// 滑鼠位置Fitness 與 座標
	JLabel Text_Fitness = new JLabel("f(x)=");

	 // 明暗軸
	static JSlider Slider_Transparency = new JSlider(0, 180, 180);
	JLabel bright = new JLabel();
	JLabel dark = new JLabel();

	// 讀取按鈕
	static JButton Button_ReadFile = new JButton();
	// 播放器按鈕
	// switch
	static JToggleButton Button_Player = new JToggleButton();
	// 照相按鈕
	JButton Button_Camera = new JButton();
	// 錄影按鈕
	static JToggleButton Button_Video = new JToggleButton();
	static int Video_mode = 0;
	static int Video_x1, Video_x2, Video_y1, Video_y2;
	static AVIOutputStream Video_out_file;
	// FB連結按鈕
	JButton Button_Facebook = new JButton();
	// 重疊獨檔
//	static JToggleButton Overlapping_ToggleButton2= new JToggleButton();
	// 版本號
	JLabel Text_GS = new JLabel("" + Show.Version);
	// 檔名
	static JLabel Input_File = new JLabel("File : ");
	// 有史以來最佳解(GBest)
	static JLabel Golbal_best = new JLabel("GBest : ");

	JSplitPane Header_split; // 上方分隔線

	// ----------中間區域
	// 維度
	static float Dimension_Mode = 2;
	static int Funtion_Dimension = 2;
	// 1Dimension
	static double Resolution = 0.01;
	static int Resolution_flag = 0;
	static int Resolution_count = 0;
	JButton Button_Borderchange = new JButton();
	// N維模式
	JPanel Middle_JPanel;
	// 外框輪廓
	static JLabel Outline_JLabel;
	static BufferedImage Outline_Image;
	// 解空間顏色
	static JLabel Answer_JLabel;
	static BufferedImage Answer_Image;
	static int Answer_Change = 1;

	// 進度軸
	JLabel Progress_JLabel;
	BufferedImage Progress_Image;
	static int Progress_Change = 1;
	static int int_Generation_Change = 1;

	// 世代數輸入
	JTextField Scan_Generation = new JTextField();
	JButton Button_Confirm = new JButton();
	static JLabel Progress_Unit = new JLabel();

	// ----------播放控制區域
	JLabel Label_temp = new JLabel();

	int Index_x1, Index_x2 = 0;
	// 圓圓的那個
	JLabel Label_Index = new JLabel();
	int Int_Index = 2;

	JPanel Bottom_JPanel;
	static JButton Button_Pause = new JButton();
	static JButton Button_Stop = new JButton();
	static JButton Button_Accelerate = new JButton();
	static JButton Button_Decelerate = new JButton();
	// 1 -> 2 -> 4 -> .....64
	static int int_Speed = 1;
	static JLabel Text_Speed = new JLabel();
	static JLabel Label_Speed = new JLabel();

	static JButton Button_Readfile = new JButton();
	static JButton Zoom_ToggleButton = new JButton();
	static JToggleButton Info_ToggleButton = new JToggleButton();
	static int Info_pick = 0;
	static int Zoom_pick = 0;
	static JToggleButton Probability_ToggleButton = new JToggleButton();
	static int Show_P = 0;

	//取現圖生成鈕
	static JButton Converse_Button = new JButton();
	
	//跳轉進度按鈕
	static JTextField GoTo_Generation = new JTextField();
	static JButton GoTo_Button = new JButton();
	static JButton GoTo_Confirm = new JButton();

	// ----------右邊區域

	// 顏色上下限設定
	static JTextField Input_Color_Caps = new JTextField("100");
	static JTextField Input_Color_Limit = new JTextField("0");

	static // ---------------------------變數宣告區
	// 模式(1:組合 2:地圖)
	int System_Mode = 1;
	static int Mode_Change = 0;

	// 世代數
	static Generation_Initial Generation[];
	static Generation_Initial temp_Generation[];
	static int Now_Generation = 1;
	static int Total_Generation = 1;
	static int Draw_Generation = 1;

	// 地圖
	static Map_Initial Map[];
	static Map_Initial temp_Map;

	// 最佳解
	static String Fitness_String[];
	static int Fitness_Amount; // 適應值數量
	static int Fitness_JLabel_len = 14;
	// static BufferedImage[] Fitness_Img = new BufferedImage[10];
	// static BufferedImage[] History_Img;
	Icon Fitness_Img;

	// 粒子Fitness(下方檔名區)
	JLabel PFitness_JLabel = new JLabel();
	String PFitness_String = new String();

	static // 怪怪的地方
	// 顏色對照工具輔助值
	// Color顏色比重 藍 青 綠 黃 紅 深紅(黑)
	int Color_Proportion[] = { 40, 40, 30, 20, 60, 40 };
	static int Color_P_Code[] = { 0, 210, 245, 255, 255, 70 };
	static double Color_Caps = 1; // 上界
	static double Color_Limit = 0; // 下界

	// 圖片
	static ImageIcon T[] = new ImageIcon[101];

	// 播放
	static int Round = 0;
	static int Speed = 16; // 倍率
	static int Pause = 2;

	// 視窗輔助值
	static int Outside_X = 5;
	static int Outside_Y = 0;
	static int Spacing = 30; // 間隔區

	static // 中間區域起點(x,y) 寬長(w,l)
	int M_block_x = Outside_X + Spacing;
	static int M_block_y = Outside_Y + Spacing;
	static int M_block_w = 1000;
	static int M_block_l = 500;

	// 右邊區域起點(x,y) 寬長(w,l)
	int R_block_x = M_block_x + M_block_w + Spacing;
	int R_block_y = M_block_y;
	int R_block_w = 35;
	int R_block_l = 0;

	// 外圍?
	int Outline_x = M_block_x + M_block_w + Spacing - Outside_X + 120;
	int Outline_y = M_block_y + M_block_l + Spacing - Outside_Y;

	// 地圖輔助值
	static double Border_X = 0;
	static double Border_Y = 0;
	static double Border_L = 100;

	static double Border_X_S = 100;
	static double Border_X_M = 100;
	static double Border_X_N = -100;

	static double Border_Y_S = 100;
	static double Border_Y_M = 100;
	static double Border_Y_N = -100;
	static double Border_Y_L = 100;
	// ---------------------------其他宣告區

	static double Postfix[] = new double[100]; // 後置運算式 輸出時的後置int運算 Postfix[0]=Postfix陣列大小 -1～-7為運算元 -8為X -9為Y
	int MouseMod = 0; // 滑鼠點擊判斷
	int MouseXago, MouseYago;
	long temp_time = 0; // 滑鼠連點兩下判斷用
	int Show_Title = 1; // 開頭EPanel文字的旗標
	static int JFrame_mod = 0; // 判斷當前是哪個JFrame

	// ------------------------------------------------------------------主視窗程式碼
	Show() {

		System.out.println("Rx=" + R_block_x);
		System.out.println("Ry=" + R_block_y);
		System.out.println("Mx=" + M_block_x);
		System.out.println("My=" + M_block_y);

		// 一開始畫面的背景

//		Expand_Equation("sum(Xi^2-10*cos(2*Pi*Xi)+10)/40",Postfix);
		// Expand_Equation("(sum(X[i]^2+X[i+1]^2))^0.48*((sin(50*(X[i]^2+X[i+1]^2)^0.1))^2+1)",Postfix);
		// Expand_Equation("sum(Xi^2-10*prod(2*Pi*Xi))",Postfix);

		// 世代宣告
		Generation = new Generation_Initial[Generation_MAX];
		temp_Generation = new Generation_Initial[Generation_MAX];
		Map = new Map_Initial[Generation_MAX];

		// ---------------------------圖片匯入
		DecimalFormat dfT = new DecimalFormat("00");
		// 按鈕圖片匯入 全從內部
		for (int i = 0; i <= Image_T_MAX; i++)
			T[i] = new ImageIcon(this.getClass().getResource("T/t" + dfT.format(i) + ".png"));

		// ---------------------------上方區域

		// 滑鼠位置 輔助用Fitness值
		Text_Fitness.setBounds(5, 0, 200, 30);
		Text_Fitness.setFont(new Font("微軟正黑體", Font.BOLD, 18));
		// Text_Fitness.setToolTipText("滑鼠位置Fitness值");

		// 明暗軸 透明度控制條
		Slider_Transparency = new JSlider(0, 360, 180);
		// Slider_Transparency.setToolTipText("解空間透明度調整控制器");
		Slider_Transparency.setBounds(Outline_x - 530, 0, 160, 35);
		Slider_Transparency.setMajorTickSpacing(180);
		Slider_Transparency.setMinorTickSpacing(30);
		Slider_Transparency.setSnapToTicks(true);
		Slider_Transparency.setPaintLabels(true);
		Slider_Transparency.addMouseListener(MyMouseAdapter);
		Slider_Transparency.addMouseMotionListener(MyMouseAdapter);
		Slider_Transparency.addKeyListener(MyKeyAdapter);
		Slider_Transparency.setBackground(new Color(180, 180, 180));
		Slider_Transparency.setPaintTicks(true);
		// Slider_Transparency.setPaintTicks(false);
		// 明暗軸 數值名稱化
		Hashtable<Integer, JLabel> table = new Hashtable<Integer, JLabel>();
		/*
		 * JLabel reds = new JLabel(); reds.setIcon(T[28]); table.put(new
		 * Integer(0),reds); table.put(new Integer(0), new JLabel("dark"));
		 * table.put(new Integer(100), new JLabel("")); table.put(new Integer(200), new
		 * JLabel("bright"));
		 */
		table.put(new Integer(0), new JLabel(""));
		table.put(new Integer(100), new JLabel(""));
		table.put(new Integer(200), new JLabel(""));
		Slider_Transparency.setLabelTable(table);

		bright.setBounds(Outline_x - 365, 3, 30, 30);
		bright.setIcon(T[28]);
		dark.setBounds(Outline_x - 555, 3, 30, 30);
		dark.setIcon(T[27]);
		// 視窗按鈕
		// 錄影
		Button_Video = new JToggleButton(T[30]);
		Button_Video.setMargin(new Insets(0, 0, 0, 0)); // 設置邊框和標籤的間距
		Button_Video.setBounds(Outline_x - 330, 0, 55, 35);
		Button_Video.setToolTipText("Record");
		Button_Video.addMouseListener(MyMouseAdapter);
		Button_Video.addKeyListener(MyKeyAdapter);
		Button_Video.setBackground(new Color(180, 180, 180));
		// 拍照
		Button_Camera = new JButton(T[29]);
		Button_Camera.setMargin(new Insets(0, 0, 0, 0)); // 設置邊框和標籤的間距
		Button_Camera.setBounds(Outline_x - 276, 0, 35, 35);
		Button_Camera.setToolTipText("Print Screen");
		Button_Camera.addMouseListener(MyMouseAdapter);
		Button_Camera.addKeyListener(MyKeyAdapter);
		Button_Camera.setBackground(new Color(180, 180, 180));
		// 播放器
		/*
		 * Button_Player= new JToggleButton(T[11]); Button_Player.setMargin(new
		 * Insets(0, 0, 0, 0)); //設置邊框和標籤的間距
		 * Button_Player.setBounds(Outline_x-215,0,35,35);
		 * Button_Player.setToolTipText("演化計算播放器");
		 * Button_Player.addMouseListener(MyMouseAdapter);
		 * Button_Player.addKeyListener(MyKeyAdapter); Button_Player.setBackground(new
		 * Color(180,180,180));
		 */
		// 讀取按鈕
		Button_ReadFile.setIcon(T[5]);
		Button_ReadFile.setMargin(new Insets(0, 0, 0, 0)); // 設置邊框和標籤的間距
		Button_ReadFile.setBounds(Outline_x - 241, 0, 35, 35);
		Button_ReadFile.setToolTipText("Read File");
		Button_ReadFile.addMouseListener(MyMouseAdapter);
		Button_ReadFile.addKeyListener(MyKeyAdapter);
		Button_ReadFile.setBackground(new Color(180, 180, 180));

		// 重疊讀檔
		/*
		 * Overlapping_ToggleButton2= new JToggleButton(Show.T[16]);
		 * Overlapping_ToggleButton2.setToolTipText("Compare Files");
		 * Overlapping_ToggleButton2.setMargin(new Insets(0, 0, 0, 0)); //設置邊框和標籤的間距
		 * Overlapping_ToggleButton2.addMouseListener(MyMouseAdapter);
		 * Overlapping_ToggleButton2.setBounds(Outline_x-206,0,35,35);
		 */
		// FB連結
		Button_Facebook = new JButton(T[13]);
		Button_Facebook.setMargin(new Insets(0, 0, 0, 0)); // 設置邊框和標籤的間距
//		Button_Facebook.setBounds(Outline_x-171,0,35,35);
		Button_Facebook.setBounds(Outline_x - 206, 0, 35, 35);
		Button_Facebook.setToolTipText("EPanel Fans Club");
		Button_Facebook.addMouseListener(MyMouseAdapter);
		Button_Facebook.addKeyListener(MyKeyAdapter);
		Button_Facebook.setBackground(new Color(180, 180, 180));

		// 版本號
//		Text_GS.setBounds(Outline_x-120,0,140,35);
		Text_GS.setBounds(Outline_x - 150, 0, 140, 35);
		Text_GS.setFont(new Font("SansSerif", Font.BOLD, 20));
		// Text_GS.setToolTipText("目前軟體版本");

		// 滑鼠位置Fit
		Text_Fitness.setBounds(5, 0, 250, 30);
		Text_Fitness.setFont(new Font("微軟正黑體", Font.BOLD, 18));

		// 檔名
		Input_File.setBounds(5, 35, 1000, 32);
		Input_File.setToolTipText("File Name");
		Input_File.setFont(new Font("微軟正黑體", Font.BOLD, 18));
//		add(Input_File);

		// 目前最佳解
		Golbal_best.setBounds(250, 0, 200, 32);
		Golbal_best.setToolTipText("GBest");
		Golbal_best.setFont(new Font("微軟正黑體", Font.BOLD, 18));

		// ---------------------------中間區域

		// 外框輪廓(白框框)
		Outline_Image = new BufferedImage(Outline_x, Outline_y, BufferedImage.TYPE_INT_RGB);
		Outline_Panel();
		Outline_JLabel = new JLabel(new ImageIcon(Outline_Image));
		Outline_JLabel.setBounds(0, 0, Outline_x, Outline_y);
		Outline_JLabel.addMouseListener(MyMouseAdapter);
		Outline_JLabel.addMouseWheelListener(MyMouseAdapter);
		Outline_JLabel.addMouseMotionListener(MyMouseAdapter);
		Outline_JLabel.addKeyListener(MyKeyAdapter);
		Outline_JLabel.setLayout(null);
		// 粒子上Fitness
		PFitness_String = "";
		PFitness_JLabel.setSize(240, 34);
		PFitness_JLabel.addMouseListener(MyMouseAdapter);
		PFitness_JLabel.setFont(new Font("微軟正黑體", Font.BOLD, 16));
		PFitness_JLabel.setForeground(new Color(255, 255, 255));
		PFitness_JLabel.setBackground(new Color(0, 0, 0, 100));
		PFitness_JLabel.setOpaque(true);
		PFitness_JLabel.setHorizontalAlignment(SwingConstants.CENTER);
		PFitness_JLabel.setLayout(null);
		PFitness_JLabel.setVisible(false);
		// 解空間
		Answer_Image = new BufferedImage(M_block_w, M_block_l, BufferedImage.TYPE_INT_RGB);
		Answer_Panel();
		Answer_JLabel = new JLabel(new ImageIcon(Answer_Image));
		Answer_JLabel.setBounds(M_block_x + 2, M_block_y + 2, M_block_w - 3, M_block_l - 3);
		Answer_JLabel.addMouseListener(MyMouseAdapter);
		Answer_JLabel.addMouseWheelListener(MyMouseAdapter);
		Answer_JLabel.addMouseMotionListener(MyMouseAdapter);
		Answer_JLabel.addKeyListener(MyKeyAdapter);
		Answer_JLabel.setLayout(null);
		// 解空間 拖移讀檔
		Answer_JLabel.setDropTarget(new DropTarget() {
			public void drop(DropTargetDropEvent e) {
				Show_Title = 0;

				e.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
				try {
					File now_file;
					List droppedFiles = (List) e.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
					now_file = (File) droppedFiles.get(0);
					// System.out.print(file.getAbsolutePath());
					// Player.RF(file);

					String SRF_now = now_file.getName();
					if (SRF_now.toLowerCase().endsWith("epin")) {
						Player.Player_file = now_file;
						Reset_Panel();

						frame_Player.setVisible(true);

						JFrame_mod = 1;
						Input_Color_Caps.setText("100");
						Input_Color_Limit.setText("0");

//for bar						GoTo_Button.setVisible(true);
					}

				} catch (Exception ex) {
					ex.printStackTrace();
				}

			}
		});

//		System.out.println(Point_MAX);
		// 解空間上 Fitness顯示
		Fitness_String = new String[Fitness_JLabel_MAX];

		// 進度軸
		Progress_Image = new BufferedImage(M_block_w + 3, 35, BufferedImage.TYPE_INT_RGB);
		Progress_Panel();
		Progress_JLabel = new JLabel(new ImageIcon(Progress_Image));
		Progress_JLabel.setBounds(M_block_x - 1, M_block_y + M_block_l - 1, M_block_w + 3, R_block_w);
		Progress_JLabel.addMouseListener(MyMouseAdapter);
		Progress_JLabel.addMouseWheelListener(MyMouseAdapter);
		Progress_JLabel.addMouseMotionListener(MyMouseAdapter);
		Progress_JLabel.addKeyListener(MyKeyAdapter);
		Progress_JLabel.setLayout(null);

		/*
		 * for bar GoTo_Button= new JButton("GOTO"); GoTo_Button.setFont(new
		 * Font("微軟正黑體",Font.BOLD,12)); //Color_ToggleButton.setToolTipText("自動調整顏色");
		 * GoTo_Button.setVisible(false); GoTo_Button.setMargin(new Insets(0, 0, 0, 0));
		 * //設置邊框和標籤的間距 GoTo_Button.addMouseListener(MyMouseAdapter);
		 * GoTo_Button.addKeyListener(MyKeyAdapter);
		 * GoTo_Button.setBounds(680,812,50,20); add(GoTo_Button);
		 */
		// 世代數輸入
		Scan_Generation.setSize(100, 22);
		Scan_Generation.setVisible(false);
		Scan_Generation.setFont(new Font("微軟正黑體", Font.BOLD, 16));
		Scan_Generation.addMouseListener(MyMouseAdapter);
		Scan_Generation.addMouseMotionListener(MyMouseAdapter);
		Scan_Generation.addKeyListener(MyKeyAdapter);
		add(Scan_Generation);

		Button_Confirm.setSize(22, 22);
		Button_Confirm.setIcon(T[36]);
		Button_Confirm.setVisible(false);
		Button_Confirm.addMouseListener(MyMouseAdapter);
		Button_Confirm.addMouseMotionListener(MyMouseAdapter);
		Button_Confirm.addKeyListener(MyKeyAdapter);
		add(Button_Confirm);

		// 進度軸單位
		Progress_Unit.setBounds(645, 805, 140, 35);
		Progress_Unit.setFont(new Font("SansSerif", Font.BOLD, 12));
		// Progress_Unit.setText("unit:generation");
		add(Progress_Unit);

////	//---------------------------播放控制區域
		Label_Index.setBounds(224, Outline_y + 85, 32, 32);
		Label_Index.setIcon(T[40]);
		Label_Index.setVisible(true);
		Label_Index.addMouseListener(MyMouseAdapter);
		Label_Index.addMouseMotionListener(MyMouseAdapter);
		add(Label_Index);
		add(Label_temp);

		Button_Stop.setBounds(0, 0, 32, 32);
		Button_Stop.setIcon(T[1]);
		Button_Stop.setVisible(true);
		Button_Stop.addMouseListener(MyMouseAdapter);
		Button_Stop.addKeyListener(MyKeyAdapter);

		Button_Pause.setBounds(32, 0, 32, 32);
		Button_Pause.setIcon(T[2]);
		Button_Pause.setVisible(true);
		Button_Pause.addMouseListener(MyMouseAdapter);
		Button_Pause.addKeyListener(MyKeyAdapter);

		Button_Decelerate.setBounds(64, 0, 32, 32);
		Button_Decelerate.setIcon(T[37]);
		Button_Decelerate.setVisible(true);
		Button_Decelerate.addMouseListener(MyMouseAdapter);
		Button_Decelerate.addKeyListener(MyKeyAdapter);

		Button_Accelerate.setBounds(96, 0, 32, 32);
		Button_Accelerate.setIcon(T[4]);
		Button_Accelerate.setVisible(true);
		Button_Accelerate.addMouseListener(MyMouseAdapter);
		Button_Accelerate.addKeyListener(MyKeyAdapter);

		Text_Speed.setBounds(128, 0, 32, 32);
		Text_Speed.setText("1");
		Text_Speed.setFont(new Font("微軟正黑體", Font.BOLD, 14));
		Text_Speed.setHorizontalAlignment(JLabel.CENTER);
		Text_Speed.setVisible(true);

		Label_Speed.setBounds(128, 0, 32, 32);
		Label_Speed.setIcon(T[9]);
		Label_Speed.setVisible(true);

		Zoom_ToggleButton.setBounds(160, 0, 32, 32);
		Zoom_ToggleButton.setIcon(T[24]);
		Zoom_ToggleButton.setVisible(true);
		Zoom_ToggleButton.addMouseListener(MyMouseAdapter);
		Zoom_ToggleButton.addKeyListener(MyKeyAdapter);

		Info_ToggleButton.setBounds(192, 0, 32, 32);
		Info_ToggleButton.setIcon(T[12]);
		Info_ToggleButton.setVisible(true);
		Info_ToggleButton.addMouseListener(MyMouseAdapter);
		Info_ToggleButton.addKeyListener(MyKeyAdapter);

		Probability_ToggleButton.setBounds(224, 0, 32, 32);
		Probability_ToggleButton.setIcon(T[18]);
		Probability_ToggleButton.setVisible(true);
		Probability_ToggleButton.addMouseListener(MyMouseAdapter);
		Probability_ToggleButton.addKeyListener(MyKeyAdapter);
		
		Converse_Button.setBounds(256,0,32,32);
		Converse_Button.setIcon(T[19]);
		Converse_Button.setVisible(true);
		Converse_Button.addMouseListener(MyMouseAdapter);

//for player		
		GoTo_Button.setBounds(288, 0, 32, 32);
		GoTo_Button.setIcon(T[22]);
		GoTo_Button.setVisible(true);
		GoTo_Button.addMouseListener(MyMouseAdapter);
		GoTo_Button.addMouseMotionListener(MyMouseAdapter);
		GoTo_Button.addKeyListener(MyKeyAdapter);

//		add(GoTo_Button);

		GoTo_Generation.setBounds(288, 0, 50, 32);
		GoTo_Generation.setVisible(false);
		GoTo_Generation.setFont(new Font("微軟正黑體", Font.BOLD, 16));
		GoTo_Generation.addMouseListener(MyMouseAdapter);
		GoTo_Generation.addMouseMotionListener(MyMouseAdapter);
		GoTo_Generation.addKeyListener(MyKeyAdapter);
		GoTo_Generation.setText("0");
		GoTo_Generation.setEditable(true);

//		add(GoTo_Generation);

		GoTo_Confirm.setBounds(338, 0, 32, 32);
		GoTo_Confirm.setIcon(T[22]);
		GoTo_Confirm.setVisible(false);
		GoTo_Confirm.addMouseListener(MyMouseAdapter);
		GoTo_Confirm.addMouseMotionListener(MyMouseAdapter);
		GoTo_Confirm.addKeyListener(MyKeyAdapter);

//		add(GoTo_Confirm);

		// ---------------------------右方區域

		// 顏色上限設定
		Input_Color_Caps.setBounds(R_block_x + R_block_w, R_block_y + 30, 40, 20);
		Input_Color_Caps.setFont(new Font("微軟正黑體", Font.BOLD, 12));
		Input_Color_Caps.setHorizontalAlignment(JTextField.CENTER);
		// Input_Color_Caps.setToolTipText("顏色上限設定");
		Input_Color_Caps.setEditable(false);
		Input_Color_Caps.addMouseListener(MyMouseAdapter);
		Input_Color_Caps.addKeyListener(MyKeyAdapter);
		// add(Input_Color_Caps);
		// 顏色下限設定
		Input_Color_Limit.setBounds(R_block_x + R_block_w, R_block_y + R_block_l + 30, 40, 20);
		Input_Color_Limit.setFont(new Font("微軟正黑體", Font.BOLD, 12));
		Input_Color_Limit.setHorizontalAlignment(JTextField.CENTER);
		// Input_Color_Limit.setToolTipText("顏色下限設定");
		Input_Color_Limit.setEditable(false);
		Input_Color_Limit.addMouseListener(MyMouseAdapter);
		Input_Color_Limit.addKeyListener(MyKeyAdapter);
		// add(Input_Color_Limit);

		// ---------------------------物件放入 畫布
		// 上面畫布
		Header_JPanel = new JPanel();
		Header_JPanel.setSize(Outline_x + 20, 67);
		Header_JPanel.setLayout(null);
		Header_JPanel.setBackground(new Color(180, 180, 180));
		Header_JPanel.add(Text_GS);
		Header_JPanel.add(Button_Video);
		Header_JPanel.add(Button_Camera);
		// Header_JPanel.add(Button_Player);
		Header_JPanel.add(Button_ReadFile);
//		
//		Header_JPanel.add(Overlapping_ToggleButton2);
//
		Header_JPanel.add(Button_Facebook);
		Header_JPanel.add(Slider_Transparency);
		Header_JPanel.add(Text_Fitness);
		Header_JPanel.add(Input_File);
		Header_JPanel.add(Golbal_best);

		Header_JPanel.add(bright);
		Header_JPanel.add(dark);

//		//播放控制畫布
		Bottom_JPanel = new JPanel();
		Bottom_JPanel.setSize(370, 32);
		Bottom_JPanel.setLocation(256, Outline_y + 85);
		Bottom_JPanel.setLayout(null);
		Bottom_JPanel.add(Button_Stop);
		Bottom_JPanel.add(Button_Pause);
		Bottom_JPanel.add(Button_Accelerate);
		Bottom_JPanel.add(Button_Decelerate);
		Bottom_JPanel.add(Text_Speed);
		Bottom_JPanel.add(Label_Speed);
		Bottom_JPanel.add(Zoom_ToggleButton);
		Bottom_JPanel.add(Info_ToggleButton);
		Bottom_JPanel.add(Probability_ToggleButton);
		Bottom_JPanel.add(Button_Borderchange);
		Bottom_JPanel.add(Converse_Button);
		Bottom_JPanel.add(GoTo_Button);
		Bottom_JPanel.add(GoTo_Generation);
		Bottom_JPanel.add(GoTo_Confirm);
		Bottom_JPanel.setVisible(true);
		add(Bottom_JPanel);

		// 中間畫布
		Middle_JPanel = new JPanel();
		Middle_JPanel.setSize(Outline_x + 20, Outline_y + 95);
		Middle_JPanel.setLayout(null);
		for (int i = 0; i < Fitness_JLabel_MAX; i++) {
			// Fitness_JLabel[i].add(new JLabel(Fitness_Img, JLabel.LEFT));
//			Middle_JPanel.add(Fitness_JLabel[i]);
//			Middle_JPanel.add(Generation_JLabel[i]);
			// Middle_JPanel.add(Fitness_Img[i]);
		}
		Middle_JPanel.add(Progress_JLabel);
		Middle_JPanel.add(Answer_JLabel);
		Middle_JPanel.add(Outline_JLabel);
		Middle_JPanel.setBackground(Color.WHITE);
		add(PFitness_JLabel);

		// Middle_JPanel.add(new JLabel(Fitness_Img, JLabel.CENTER));

		// 視窗配置 ((只能放在這裡))
		setLayout(new BorderLayout());

		// 分隔線 -----------------上中兩個區域
		Header_split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, false, Header_JPanel, Middle_JPanel);
		Header_split.setDividerLocation(70);
		Header_split.setEnabled(false);
		add(Header_split, BorderLayout.CENTER);

		// 計時器 0.005s
		Timer timer = new Timer(5, this);
		timer.start();

		// 視窗設定
		setResizable(false);
		setIconImage(T[10].getImage()); // icon設定
		setBounds(0, 0, Outline_x + 20, Outline_y + 160);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("EPanel " + Show.Version);
		addWindowListener(MyWindowListener);
		addKeyListener(MyKeyAdapter);
		addMouseListener(MyMouseAdapter);

		Answer_JLabel.requestFocus();
	}

	// ------------------------------------------------------------------計時器
	public void actionPerformed(ActionEvent e) {
		// 解空間重畫
		if (Answer_Change == 1) {
//			System.out.println("------1");
			if (System_Mode == 1) {
				if (Mode_Change == 1) {
					setBounds(0, 0, Outline_x + 20, Outline_y + 160);
					Answer_JLabel.setBounds(M_block_x + 2, M_block_y + 2, M_block_w - 3, M_block_l - 3);
					Progress_JLabel.setBounds(M_block_x - 1, M_block_y + M_block_l - 1, M_block_w + 3, R_block_w);
					Text_Fitness.setBounds(5, 0, 200, 30);
					Text_Fitness.setText("f(x)=");
					Golbal_best.setBounds(250, 0, 200, 32);
					Slider_Transparency.setVisible(true);
					dark.setVisible(true);
					bright.setVisible(true);
					Button_Video.setBounds(Outline_x - 330, 0, 55, 35);
					Button_Camera.setBounds(Outline_x - 276, 0, 35, 35);
					Button_ReadFile.setBounds(Outline_x - 241, 0, 35, 35);
					Button_Facebook.setBounds(Outline_x - 206, 0, 35, 35);
					Text_GS.setBounds(Outline_x - 150, 0, 140, 35);

					Mode_Change = 0;
				}
//				System.out.println("------2");
				Outline_Panel();
				Answer_Panel();
				repaint();
				Answer_Change = 0;

//				System.out.println(Generation[Now_Generation].Amount);
				// 暫停中
				if (Pause == 1 && Total_Generation >= 1) {
					Outline_Panel();
					Drawer_Panel();
				}
			} else if (System_Mode == 2) {
				if (Mode_Change == 1) {
					setBounds(0, 0, Outline_x - (M_block_w / 2) + 20, Outline_y + 160);
					Answer_JLabel.setBounds(M_block_x + 2, M_block_y + 2, (M_block_w / 2) - 3, M_block_l - 3);
					Progress_JLabel.setBounds(M_block_x - 1, M_block_y + M_block_l - 1, (M_block_w / 2) + 3, R_block_w);
					Text_Fitness.setBounds(130, 0, 500, 35);
					Golbal_best.setBounds(5, 0, 200, 32);
					Slider_Transparency.setVisible(false);
					dark.setVisible(false);
					bright.setVisible(false);
					Button_Video.setBounds(420, 0, 55, 35);
					Button_Camera.setBounds(475, 0, 35, 35);
					Button_ReadFile.setBounds(510, 0, 35, 35);
					Button_Facebook.setBounds(545, 0, 35, 35);
					Text_GS.setBounds(600, 0, 140, 35);
					Mode_Change = 0;

					Probability_ToggleButton.setEnabled(false);
				}
//				System.out.println("------3");
				Outline_Panel2();
				Answer_Panel2();
				repaint();
				Answer_Change = 0;

				if (Pause == 1 && Total_Generation >= 1) {
					Outline_Panel2();
					Drawer_Panel2();
//					setBounds(0,0,Outline_x+8-(M_block_w/2),Outline_y+150);
				}

				if (Show.Now_Generation == Show.Total_Generation)
					Probability_ToggleButton.setEnabled(true);
				else
					Probability_ToggleButton.setEnabled(false);
			}
		}

		// 播放中
		if (Pause == 0) {
			if (System_Mode == 1) {
				// Answer_Change=1;
				if (Show.Now_Generation < Show.Total_Generation) {
					// 進入下一個世代
					if (Round >= 4096) {
						Round = 0;
						Now_Generation++;
						Progress_Change = 1;
						Player.Slider_Generation.setValue(Now_Generation);
						Outline_Panel();
						Drawer_Panel();
					}
					Round += Speed;
				} else {
					Show.Pause = 1;
					Player.Button_Start.setIcon(Show.T[2]);
					Button_Pause.setIcon(Show.T[2]);
				}
			} else if (System_Mode == 2) {
				if (Show.Now_Generation < Show.Total_Generation) {
					// 進入下一個世代
					if (Round >= 4096) {
						Round = 0;
						Now_Generation++;

						Progress_Change = 1;
						Player.Slider_Generation.setValue(Now_Generation);
						Outline_Panel2();
						Drawer_Panel2();
					}
					Round += Speed;
				} else {
					Show.Pause = 1;
					Player.Button_Start.setIcon(Show.T[2]);
					Button_Pause.setIcon(Show.T[2]);
					Probability_ToggleButton.setEnabled(true);
				}
			}
		}
		Player.Text_Generation.setText("" + Now_Generation + "/" + Total_Generation);

		// 世代數改變
		if (int_Generation_Change > 0 && Fitness_Amount > 0) {
			Generation_Change();
		}

		if (Progress_Change == 1) {
			Progress_Change = 0;
			Progress_Panel();
			repaint();
		}

	}

	// 第一層 外框輪廓
	// 第一層 輪廓外框
	void Outline_Panel() {
		Graphics g = Outline_Image.getGraphics();
		Graphics2D g2D = (Graphics2D) g;
		int temp_c = 0;
		float color_x = 0;

		// 數值都設為無小數
		DecimalFormat df0 = new DecimalFormat("####0");

		// 底部白
		g.setColor(new Color(255, 255, 255));
		g.fillRect(0, 0, Outline_x + 20, Outline_y + 90);

		// 框線 黑色 粗(3)框
		g.setColor(new Color(0, 0, 0));
		g2D.setStroke(new BasicStroke(3.0f));

		// Middle block中間外框
		g.drawRect(M_block_x, M_block_y, M_block_w, M_block_l);

		// x y軸單位
		if (Fitness_Amount > 0) {
			g2D.setFont(new Font("微軟正黑體", Font.BOLD, 12));
			g2D.drawString("bit", 15, 13);
			if (Zoom_pick == 0)
				g2D.drawString("gen.", 5, 30);
			else
				g2D.drawString("par.", 2, 30);
		}

		if (Show_Title == 0) {
			// 顯示色碼表
			if (Show_P == 1) {
				g.setColor(new Color(0, 0, 0));
				g2D.setStroke(new BasicStroke(2.0f));
				g.drawRect(M_block_x + M_block_w + 90, M_block_y, 30, M_block_l - 30);
				for (int j = 0; j < (M_block_l - 32); j++) {
					color_x = (float) j / (M_block_l - 32);
					// System.out.println("j="+j);
					// System.out.println("color_x="+color_x);
					// System.out.println("temp_c="+temp_c);
					temp_c = ColorDeploy(color_x);
					g.setColor(new Color(temp_c / 1000000, (temp_c / 1000) % 1000, temp_c % 1000));
					g.fillRect(M_block_x + M_block_w + 91, M_block_y + M_block_l - 32 - j, 28, 1);
				}
				g.setColor(new Color(0, 0, 0));
				g2D.setFont(new Font("Nyala", Font.PLAIN, 18));
				g2D.drawString("0", M_block_x + M_block_w + 125, M_block_y + 2);
				g2D.drawString("1", M_block_x + M_block_w + 125, M_block_y + M_block_l - 28);
				float k = 0;
				for (int j = 1; j < 5; j++) {
					k += 0.2;
					// System.out.println("k="+k);
					g2D.drawString("" + k, M_block_x + M_block_w + 125, M_block_y + 2 + (M_block_l - 32) / 5 * j);
					g.drawLine(M_block_x + M_block_w + 116, M_block_y - 2 + (M_block_l - 32) / 5 * j,
							M_block_x + M_block_w + 120, M_block_y - 2 + (M_block_l - 32) / 5 * j);
				}
				// 圖示 黑1 白0
				g.setColor(new Color(0, 0, 0));
				g2D.setStroke(new BasicStroke(2.0f));
				g.drawRect(M_block_x + M_block_w + 91, M_block_y + M_block_l - 20, 10, 10);
				g.fillRect(M_block_x + M_block_w + 90, M_block_y + M_block_l - 8, 12, 12);
				g2D.setFont(new Font("微軟正黑體", Font.BOLD, 14));
				g2D.drawString(":0", M_block_x + M_block_w + 105, M_block_y + M_block_l - 10);
				g2D.drawString(":1", M_block_x + M_block_w + 105, M_block_y + M_block_l + 2);
				g2D.setFont(new Font("微軟正黑體", Font.BOLD, 16));
				g2D.drawString("Fitness", M_block_x + M_block_w + 10, 25);
			} else {
				// 圖示 黑1 白0
				g.setColor(new Color(0, 0, 0));
				g2D.setStroke(new BasicStroke(2.0f));
				g.drawRect(M_block_x + M_block_w + 6, 5, 10, 10);
				g.fillRect(M_block_x + M_block_w + 5, 18, 12, 12);
				g2D.setFont(new Font("微軟正黑體", Font.BOLD, 14));
				g2D.drawString(":0", M_block_x + M_block_w + 20, 14);
				g2D.drawString(":1", M_block_x + M_block_w + 20, 29);
				g2D.setFont(new Font("微軟正黑體", Font.BOLD, 16));
				g2D.drawString("Fitness", M_block_x + M_block_w + 40, 25);
			}
		}
	}

	// 第二層 色版
	void Answer_Panel() {
		Graphics g = Answer_Image.getGraphics();
		Graphics2D g2D = (Graphics2D) g;

		// 中間底部白
		g.setColor(new Color(255, 255, 255));
		g.fillRect(0, 0, M_block_w, M_block_l);

		int tmp = 1;
		int x = 0;
		int y = 0;

		// 初始頁面
		if (Show_Title == 1) {
			for (int i = 0; i < 25; i++) {
				for (int j = 0; j < 50; j++) {
					if (tmp == 1) {
						g.setColor(new Color(0, 0, 0));
						g.fillRect(x, y, 20, 20);
						tmp = tmp * -1;
					} else {
						g.setColor(new Color(255, 255, 255));
						g.fillRect(x, y, 20, 20);
						tmp = tmp * -1;
					}
					x += 20;
				}
				x = 0;
				y += 20;
				tmp = tmp * -1;
			}
			g.setColor(new Color(255, 255, 255, 180));
			g.fillRect(0, 0, M_block_w, M_block_l);
			g.setColor(new Color(255, 255, 255, 255));
			g2D.setFont(new Font("Nyala", Font.BOLD, 200));
			g2D.drawString("EPanel 2.0", 85, 325);
			g.setColor(new Color(0, 0, 0, 255));
			g2D.setFont(new Font("Nyala", Font.BOLD, 200));
			g2D.drawString("EPanel 2.0", 80, 320);
		}
	}

	// 粒子顯示
	static void Drawer_Panel() {
		Graphics g = Answer_Image.getGraphics();
		Graphics2D g2D = (Graphics2D) g;

		Graphics g2 = Outline_Image.getGraphics();
		Graphics2D g22D = (Graphics2D) g2;
		g2.setColor(new Color(0, 0, 0));
		g22D.setFont(new Font("微軟正黑體", Font.BOLD, 15));

		int bit_num = 1;
		float bit_w = M_block_w;
		float bit_l = (float) M_block_l / (float) Particle_shownum;
		float now_x = 0;
		float now_y = 0;
		int text_size = 8;
		int color = Slider_Transparency.getValue();
		
		if(color>255) color = 255;

		DecimalFormat df0 = new DecimalFormat("####0");
		DecimalFormat df1 = new DecimalFormat("#0.00");

//		System.out.println("Now_Generation="+Now_Generation);

		// 顯示每世代最佳解(Zoom out)
		if (Zoom_pick == 0) {
			// 顯示的世代數數量
			Particle_shownum = 20;

			Draw_Generation = Now_Generation - (Particle_shownum - 2);
			if (Draw_Generation < 1)
				Draw_Generation = 1;

			bit_l = (float) M_block_l / (float) Particle_shownum;

			// 第i代
			for (int i = Draw_Generation; i <= Now_Generation; i++) {
				Golbal_best.setText("GBest : " + Generation[i].GBest);
//				System.out.println("GBest="+Generation[i].GBest);
				// 取GBest位置
//				System.out.println("GBest="+Generation[i].CBest);
				int j = Generation[i].CBest;
				now_x = 0;
				bit_num = Generation[i].X[j].Amount;
				bit_w = (float) M_block_w / (float) bit_num;
				int temp_c;
//				System.out.println("bit_w="+bit_w);
//				System.out.println("bit_l="+bit_l);

				for (int k = 0; k < bit_num; k++) {
//					System.out.println("bit_p%="+Generation[i].X[j].Probability[k]);
//					System.out.println("bit_p="+Generation[i].X[j].Probability[k]*100);
//					System.out.println("bit_c="+temp_c);
					// 顯示機率
					if (Show_P == 1) {
//						System.out.println("bit_p%="+Generation[i].X[j].Probability[k]);
//						System.out.println("now_x="+now_x);
//						System.out.println("now_y="+now_y);
						temp_c = ColorDeploy(Generation[i].X[j].Probability[k]);
						g.setColor(new Color(temp_c / 1000000, (temp_c / 1000) % 1000, temp_c % 1000));
						g.fillRect((int) now_x, (int) now_y, (int) bit_w, (int) bit_l);
						g.setColor(new Color(255, 255, 255));
						g.drawRect((int) now_x, (int) now_y, (int) bit_w, (int) bit_l);

						if (Info_pick == 1) {
							g.setColor(new Color(0, 0, 0));
							g2D.setFont(new Font("SansSerif", Font.PLAIN, text_size));
							g.drawString(df1.format(Generation[i].X[j].Probability[k]), (int) (now_x + text_size / 3),
									(int) (now_y + text_size * 2));
						}
					} else {
						if (Generation[i].X[j].Bit[k] == 1) {
							g.setColor(new Color(0, 0, 0));
							g.fillRect((int) now_x, (int) now_y, (int) bit_w, (int) bit_l);
							g.setColor(new Color(255, 255, 255));
							g.drawRect((int) now_x, (int) now_y, (int) bit_w, (int) bit_l);
						} else {
							g.setColor(new Color(255, 255, 255));
							g.fillRect((int) now_x, (int) now_y, (int) bit_w, (int) bit_l);
							g.setColor(new Color(0, 0, 0));
							g.drawRect((int) now_x, (int) now_y, (int) bit_w, (int) bit_l);
						}
					}
					g.setColor(new Color(0, 0, 0));
					// 每10個bit標記1次
					g22D.setFont(new Font("微軟正黑體", Font.BOLD, 10));
					if (i == Draw_Generation && k % 10 == 0)
						g2.drawString(df0.format(k + 1), /* X */(int) now_x + 35, /* Y */(int) now_y + 27);
					now_x += bit_w;
				}
				// 非當代須透明化(220 180)
				
				if (i != Now_Generation && Show_P == 0) {
					g.setColor(new Color(255, 255, 255, color));
					g.fillRect(0, (int) now_y, M_block_w, (int) bit_l);
				} else if (Info_pick == 1 && Show_P == 0) {
					g.setColor(new Color(255, 255, 255, color));
					g.fillRect(370, (int) now_y, 260, (int) now_y + ((int) bit_l / 2) + 3);
				}
				// 顯示粒子文字
				if (Info_pick == 1 && Show_P == 0) {
					g.setColor(new Color(0, 0, 0));
					g2D.setFont(new Font("IrisUPC", Font.BOLD, 25));
					g2D.drawString(Generation[i].X[j].PInfo, 380, now_y + ((int) bit_l / 2) + 7);
//					System.out.println("now_y="+now_y);
//					System.out.println("bit_h="+(now_y+(bit_l/2)));
//					System.out.println("bit_l="+bit_l);
//					System.out.println("bit_l/2="+(bit_l/2));
				}

				// 後面寫Fitness
				g22D.setFont(new Font("微軟正黑體", Font.BOLD, 15));
				if (Show_P == 1)
					g22D.drawString("" + Generation[i].X[j].Fitness, M_block_x + M_block_w + 5,
							M_block_y + now_y + ((int) bit_l / 2) + 7);
				else
					g22D.drawString("" + Generation[i].X[j].Fitness, M_block_x + M_block_w + 35,
							M_block_y + now_y + ((int) bit_l / 2) + 7);

				// 每10個粒子標記1次
				if ((i - Draw_Generation) % 10 == 0) {
					int m;
					if (i < 100)
						m = 18;
					else if (i < 1000)
						m = 12;
					else
						m = 6;
					g22D.setFont(new Font("微軟正黑體", Font.BOLD, 10));
					g2.drawString(df0.format(i), /* X */(int) m, /* Y */(int) now_y + 43);
				}
				now_y += bit_l;
//				System.out.println("-----");
			}
			// 畫目前最佳解
			int N_i = Generation[Now_Generation].GBest_gen;
			int N_j = Generation[N_i].CBest;
			bit_num = Generation[N_i].X[N_j].Amount;
			now_x = 0;
			for (int k = 0; k < bit_num; k++) {
				if (Generation[N_i].X[N_j].Bit[k] == 1) {
					if (Show_P == 0)
						g.setColor(new Color(255, 0, 0));
					else
						g.setColor(new Color(0, 0, 0));
					g.fillRect((int) now_x, (int) now_y, (int) bit_w, (int) bit_l);
					g.setColor(new Color(255, 255, 255));
					g.drawRect((int) now_x, (int) now_y, (int) bit_w, (int) bit_l);
				} else {
					g.setColor(new Color(255, 255, 255));
					g.fillRect((int) now_x, (int) now_y, (int) bit_w, (int) bit_l);
					g.setColor(new Color(0, 0, 0));
					g.drawRect((int) now_x, (int) now_y, (int) bit_w, (int) bit_l);
				}
				g.setColor(new Color(0, 0, 0));
				now_x += bit_w;
			}
			// 顯示機率num
			if (Info_pick == 1 && Show_P == 0) {
				g.setColor(new Color(255, 255, 255, 180));
				g.fillRect(370, (int) now_y, 260, (int) (now_y + (bit_l / 2) + 3));
				g.setColor(new Color(0, 0, 0));
				g2D.setFont(new Font("IrisUPC", Font.BOLD, 25));
				g2D.drawString(Generation[N_i].X[N_j].PInfo, 380, now_y + ((int) bit_l / 2) + 7);
			} else if (Show_P == 1)
				g.drawLine(0, (int) now_y, (int) now_x, (int) now_y);
			g2.setColor(new Color(255, 0, 0));
			g2.drawString("GB", /* X */(int) 10, /* Y */(int) now_y + 43);
			g22D.setFont(new Font("微軟正黑體", Font.BOLD, 15));
			if (Show_P == 1)
				g22D.drawString("" + Generation[N_i].X[N_j].Fitness, M_block_x + M_block_w + 5,
						M_block_y + now_y + ((int) bit_l / 2) + 7);
			else
				g22D.drawString("" + Generation[N_i].X[N_j].Fitness, M_block_x + M_block_w + 35,
						M_block_y + now_y + ((int) bit_l / 2) + 7);
		}
		// 每代顯示全部粒子(Zoom in)
		else {
			int i = Now_Generation;
			Particle_shownum = Generation[i].Particle;
			bit_l = (float) M_block_l / (float) Particle_shownum;
			for (int j = 0; j < Particle_shownum; j++) {
				bit_num = Generation[i].X[j].Amount;
				now_x = 0;
				bit_w = (float) M_block_w / (float) bit_num;
//				System.out.println("bit_l="+bit_l);
//				System.out.println("bit_w="+bit_w);
				for (int k = 0; k < bit_num; k++) {
//					System.out.println("bit="+Generation[i].X[j].Bit[k]);
					if (Generation[i].X[j].Bit[k] == 1) {
						if (j == Generation[i].CBest)
							g.setColor(new Color(255, 0, 0));
						else
							g.setColor(new Color(0, 0, 0));
						g.fillRect((int) now_x, (int) now_y, (int) bit_w, (int) bit_l);
						g.setColor(new Color(255, 255, 255));
						g.drawRect((int) now_x, (int) now_y, (int) bit_w, (int) bit_l);
					} else {
						g.setColor(new Color(255, 255, 255));
						g.fillRect((int) now_x, (int) now_y, (int) bit_w, (int) bit_l);
						g.setColor(new Color(0, 0, 0));
						g.drawRect((int) now_x, (int) now_y, (int) bit_w, (int) bit_l);
					}
					g.setColor(new Color(0, 0, 0));
					// 每10個標記1次
					g22D.setFont(new Font("微軟正黑體", Font.BOLD, 10));
					if (j == 0 && k % 10 == 0)
						g2.drawString(df0.format(k + 1), /* X */(int) now_x + 35, /* Y */(int) now_y + 27);
					now_x += bit_w;
				}
				/*
				 * //顯示粒子文字 g.setColor(new Color(255, 255, 255, 180)); g.fillRect(430,
				 * now_y+((int)bit_l/2)-6, 140, now_y+((int)bit_l/2)+3); g.setColor(new
				 * Color(0,0,0)); g2D.setFont(new Font("IrisUPC", Font.BOLD,25));
				 * g2D.drawString(Generation[i].X[j].PInfo, 450, now_y+((int)bit_l/2)+7);
				 */
				// 後面寫Fitness
				g22D.setFont(new Font("微軟正黑體", Font.BOLD, 15));
				g22D.drawString("" + Generation[i].X[j].Fitness, M_block_x + M_block_w + 35,
						M_block_y + now_y + ((int) bit_l / 2) + 7);

				// 每10個粒子標記1次
				if (j % 10 == 0) {
					int m;
					if (i < 100)
						m = 18;
					else
						m = 12;
					g22D.setFont(new Font("微軟正黑體", Font.BOLD, 10));
					g2.drawString(df0.format(j + 1), /* X */(int) m, /* Y */(int) now_y + 43);
				}
				now_y += bit_l;
			}
		}
//		System.out.println("---------");
	}

	void Outline_Panel2() {
		Graphics g = Outline_Image.getGraphics();
		Graphics2D g2D = (Graphics2D) g;
		Graphics g2 = Answer_Image.getGraphics();
		Graphics2D g22D = (Graphics2D) g;
		int temp_c = 0;
		float color_x = 0;
		// 數值都設為無小數
		DecimalFormat df0 = new DecimalFormat("####0");
		DecimalFormat df1 = new DecimalFormat("#0.0");

//		Text_Fitness.setText("Limit : "+Map[1].limit);

		// 底部白
		g.setColor(new Color(255, 255, 255));
		g.fillRect(0, 0, Outline_x + 20, Outline_y + 90);

		// 框線 黑色 粗(3)框
		g.setColor(new Color(0, 0, 0));
		g2D.setStroke(new BasicStroke(3.0f));
		g.drawRect(M_block_x, M_block_y, M_block_w / 2, M_block_l);

		// 色碼表
		g.setColor(new Color(0, 0, 0));
		g2D.setStroke(new BasicStroke(2.0f));
		g.drawRect(M_block_x + (M_block_w / 2) + 50, M_block_y, 30, M_block_l + R_block_w - 26);
		for (int j = 0; j < (M_block_l + R_block_w - 28); j++) {
			color_x = (float) j / (M_block_l + R_block_w - 28);
//				System.out.println("j="+j);
//				System.out.println("color_x="+color_x);
//				System.out.println("temp_c="+temp_c);
			temp_c = ColorDeploy(color_x);
			g.setColor(new Color(temp_c / 1000000, (temp_c / 1000) % 1000, temp_c % 1000));
			g.fillRect(M_block_x + (M_block_w / 2) + 51, M_block_y + M_block_l + R_block_w - 28 - j, 28, 1);
			/*
			 * if(color_x<Map[1].limit) { g.setColor(new Color(255,255,255,150));
			 * g.fillRect(M_block_x+(M_block_w/2)+51, M_block_y+M_block_l+R_block_w-28-j,
			 * 28, 1); }
			 */ }
		g.setColor(new Color(0, 0, 0));
		g2D.setFont(new Font("Nyala", Font.PLAIN, 18));
		g2D.drawString("1", M_block_x + (M_block_w / 2) + 85, M_block_y + 2);
		g2D.drawString("0", M_block_x + (M_block_w / 2) + 85, M_block_y + M_block_l + R_block_w - 26);
		float k = 0;
		for (int j = 9; j > 0; j--) {
			k += 0.1;
//				System.out.println("k="+k);
			g2D.drawString("" + df1.format(k), M_block_x + (M_block_w / 2) + 85,
					M_block_y + 2 + (M_block_l + R_block_w - 28) / 10 * j);
			g.drawLine(M_block_x + (M_block_w / 2) + 76, M_block_y - 2 + (M_block_l + R_block_w - 28) / 10 * j,
					M_block_x + (M_block_w / 2) + 80, M_block_y - 2 + (M_block_l + R_block_w - 28) / 10 * j);
		}
		g.fillRect(M_block_x + (M_block_w / 2) + 49, M_block_y + M_block_l + R_block_w - 20, 15, 15);
		g2D.setFont(new Font("Nyala", Font.PLAIN, 20));
		g2D.drawString(":sensor", M_block_x + (M_block_w / 2) + 65, M_block_y + M_block_l + R_block_w - 8);
	}

	void Answer_Panel2() {
		Graphics g = Answer_Image.getGraphics();
		Graphics2D g2D = (Graphics2D) g;

		// 中間底部白
		g.setColor(new Color(255, 255, 255));
		g.fillRect(0, 0, M_block_w, M_block_l);

//		g.setColor(new Color(0,0,0));
//		g2D.setStroke(new BasicStroke(3.0f));
//		g.drawRect(0,0,M_block_l,M_block_l);

//		g.setColor(new Color(0,255,0));
//		g.fillRect((int)0, (int)0, (int)50, (int)50);
	}

	void Drawer_Panel2() {
		Graphics g = Answer_Image.getGraphics();
		Graphics2D g2D = (Graphics2D) g;
		Graphics g2 = Outline_Image.getGraphics();
		Graphics2D g22D = (Graphics2D) g2;
		g2.setColor(new Color(0, 0, 0));
		g22D.setFont(new Font("微軟正黑體", Font.BOLD, 10));

		float sensor_w = (float) M_block_l / Map[Now_Generation].row;
		float sensor_l = (float) M_block_l / Map[Now_Generation].col;
		float now_x = 0;
		float now_y = 0;
		int temp_c;
		int text_size = 9;
//		double limit;

		DecimalFormat df0 = new DecimalFormat("#0.000");
		DecimalFormat df1 = new DecimalFormat("#0");

		Golbal_best.setText("GBest : " + Map[Now_Generation].Count);
//		limit = Map[Now_Generation].limit;

		for (int j = 0; j < Map[Now_Generation].col; j++) {
			for (int k = 0; k < Map[Now_Generation].row; k++) {
				if (((k + 1) % 5 == 1) && j == 0) {
					g2.drawString(df1.format(k + 1), /* X */(int) now_x + 35, /* Y */(int) 27);
//					System.out.println("x="+(now_x+35));
				}
				if (Show_P == 0) {
					if (Map[Now_Generation].Probability[j][k] == -1) {
						g.setColor(new Color(0, 0, 0));
						g.fillRect((int) now_x + 250, (int) now_y, (int) sensor_w, (int) sensor_l);
						g.setColor(new Color(255, 255, 255));
						g.drawRect((int) now_x + 250, (int) now_y, (int) sensor_w, (int) sensor_l);
					} else {
						temp_c = ColorDeploy(Map[Now_Generation].Probability[j][k]);
						g.setColor(new Color(temp_c / 1000000, (temp_c / 1000) % 1000, temp_c % 1000));
						g.fillRect((int) now_x + 250, (int) now_y, (int) sensor_w, (int) sensor_l);
						g.setColor(new Color(255, 255, 255));
						g.drawRect((int) now_x + 250, (int) now_y, (int) sensor_w, (int) sensor_l);
						if (Map[Now_Generation].Probability[j][k] < Map[Now_Generation].limit[j][k]) {
//							g.setColor(new Color(255,255,255,200));
//							g.fillRect((int)now_x+250, (int)now_y, (int)sensor_w, (int)sensor_l);
							g.setColor(new Color(255, 0, 0));
//							g2D.setStroke(new BasicStroke(2.0f));
							g.drawLine((int) now_x + 251, (int) now_y + 1, (int) (now_x + 251 + sensor_w),
									(int) (now_y + sensor_l + 1));
							g.drawLine((int) now_x + 250, (int) (now_y + sensor_l), (int) (now_x + 250 + sensor_w),
									(int) now_y);

						}

						if (Info_pick == 1) {
							g.setColor(new Color(255, 255, 255, 180));
							// g2D.setFont(new Font("SansSerif", Font.PLAIN, 8));
							// g2D.setFont(new Font("SansSerif", Font.PLAIN, 27));
							g2D.setFont(new Font("SansSerif", Font.PLAIN, 150 / Map[Now_Generation].row));
							if ((150 / Map[Now_Generation].row) > 3)
								// g.fillRect((int)(now_x+sensor_w/4+250-3), (int)(now_y+sensor_l/4+1),
								// (int)sensor_w/2+8, (int)sensor_l/2);
								g.fillRect((int) (now_x + sensor_w / 8 + 250),
										(int) (now_y + sensor_l / 2
												- (150 / Map[Now_Generation].row + 4) / 2/* /8*3 */),
										(int) (sensor_w / 8 * 6 + 1), (int) 150 / Map[Now_Generation].row + 4);
							g.setColor(new Color(0, 0, 0));
							if ((150 / Map[Now_Generation].row) > 3)
								g2D.drawString(df0.format(Map[Now_Generation].Probability[j][k]),
										(int) (now_x + 250 + sensor_w / 2
												- (150 / Map[Now_Generation].row) * 1.3/* +text_size/2 */),
										(int) (now_y + sensor_l / 2 + (150 / Map[Now_Generation].row) / 2
												- 1/* now_y+sensor_l/2+11+text_size/2 */));
							// g2D.drawString(df0.format(Map[Now_Generation].Probability[j][k]),(int)((j-Border_X+Border_L+0.1)*350/Border_L),700-(int)((k-Border_Y+Border_L+0.95-0.525)*350/Border_L));
							// System.out.println("x ="+now_x);
							// System.out.println("y ="+now_y);
							// System.out.println("w ="+sensor_w);
							// System.out.println("l ="+sensor_l);
							// System.out.println("xw="+(now_x+sensor_w/2));
							// System.out.println("yw="+(now_y+sensor_l/2));
						}
					}
				} else {
					if (temp_Map.Probability[j][k] == -1) {
						g.setColor(new Color(0, 0, 0));
						g.fillRect((int) now_x + 250, (int) now_y, (int) sensor_w, (int) sensor_l);
						g.setColor(new Color(255, 255, 255));
						g.drawRect((int) now_x + 250, (int) now_y, (int) sensor_w, (int) sensor_l);
					} else {
						temp_c = ColorDeploy(temp_Map.Probability[j][k]);
						g.setColor(new Color(temp_c / 1000000, (temp_c / 1000) % 1000, temp_c % 1000));
						g.fillRect((int) now_x + 250, (int) now_y, (int) sensor_w, (int) sensor_l);
						g.setColor(new Color(255, 255, 255));
						g.drawRect((int) now_x + 250, (int) now_y, (int) sensor_w, (int) sensor_l);
						if (temp_Map.Probability[j][k] < Map[Now_Generation].limit[j][k]) {
//							g.setColor(new Color(255,255,255,200));
//							g.fillRect((int)now_x+250, (int)now_y, (int)sensor_w, (int)sensor_l);
							g.setColor(new Color(255, 0, 0));
							g2D.setStroke(new BasicStroke(2.0f));
							g2D.drawLine((int) now_x + 250, (int) now_y, (int) (now_x + 250 + sensor_w),
									(int) (now_y + sensor_l));
						}

						if (Info_pick == 1) {
							g.setColor(new Color(255, 255, 255, 180));
							g2D.setFont(new Font("SansSerif", Font.PLAIN, 150 / temp_Map.row));
							if ((150 / temp_Map.row) > 3)
								g.fillRect((int) (now_x + sensor_w / 8 + 250),
										(int) (now_y + sensor_l / 2 - (150 / temp_Map.row + 4) / 2/* /8*3 */),
										(int) (sensor_w / 8 * 6 + 1), (int) 150 / Map[Now_Generation].row + 4);
							g.setColor(new Color(0, 0, 0));
							if ((150 / temp_Map.row) > 3)
								g2D.drawString(df0.format(temp_Map.Probability[j][k]),
										(int) (now_x + 250 + sensor_w / 2
												- (150 / temp_Map.row) * 1.3/* +text_size/2 */),
										(int) (now_y + sensor_l / 2 + (150 / Map[Now_Generation].row) / 2
												- 1/* now_y+sensor_l/2+11+text_size/2 */));
						}
					}
				}
				now_x += sensor_w;
			}
			if ((j + 1) % 5 == 1) {
				g2.drawString(df1.format(j + 1), /* X */(int) 20, /* Y */(int) now_y + 39);
//				System.out.println("x="+(now_y+35));
			}
			now_x = 0;
			now_y += sensor_l;
		}

//		g.setColor(new Color(0,255,0));
//		g.fillRect((int)0, (int)0, (int)50, (int)50);
	}

	// 進度軸
	void Progress_Panel() {
		Graphics g = Progress_Image.getGraphics();
		Graphics2D g2D = (Graphics2D) g;
		// 擺放時x軸位移植250
		int x_tmp = 0;
		if (System_Mode == 1)
			x_tmp = 0;
		else if (System_Mode == 2)
			x_tmp = 250;

		// 底部白
		// g.setColor(new Color(150,150,150));
		g.setColor(new Color(255, 255, 255));
		g.fillRect(0, 0, M_block_w + 4, R_block_w);

		int code_temp = 0;
		double temp = 0;

		// 進度條顏色
		if (JFrame_mod == 1)
			temp = (double) (Now_Generation - 1) / (double) (Total_Generation - 1);

		code_temp = (int) (temp * 100);
		/*
		 * if(Overlapping_ToggleButton2.isSelected() ) g.setColor(new
		 * Color(50-code_temp/2,120+code_temp,155+code_temp)); else
		 */ g.setColor(new Color(50, 155 + code_temp, 50 - code_temp / 2));

		g.fillRect(0 + x_tmp, 0, (int) (temp * (M_block_w - x_tmp * 2)) + 4, R_block_w);

		g.setColor(new Color(255, 255, 255, 100));
		g.fillRect(0 + x_tmp, 5, (int) (temp * (M_block_w - x_tmp * 2)) + 4, 5);

		g.setColor(new Color(0, 0, 0, 20));
		g.fillRect(0 + x_tmp, 20, (int) (temp * (M_block_w - x_tmp * 2)) + 4, R_block_w);

		g.setColor(new Color(0, 0, 0, 50));
		g.fillRect(0 + x_tmp, 27, (int) ((M_block_w - x_tmp * 2) * temp) + 4, R_block_w);

		// 數值
		String Str_num;
		if (JFrame_mod == 1) {
			Str_num = "" + Now_Generation + "/" + Total_Generation;
//for bar
//			GoTo_Button.setVisible(true);

			/*
			 * if(Overlapping_ToggleButton2.isSelected()) Str_num=Str_num+"(eval.)";
			 * //Progress_Unit.setText("unit:eval."); else
			 */ Str_num = Str_num + "(gener.)";
			// Progress_Unit.setText("unit:generation");
		} else
			Str_num = "";

		g.setColor(new Color(0, 0, 0));
		// LilyUPC Impact 微軟正黑體 Consolas

		g2D.setFont(new Font("IrisUPC", Font.BOLD, 40));
		g2D.drawString(Str_num, (M_block_w - Str_num.length() * 19) / 2, R_block_w / 2 + 10);

		g.setColor(new Color(0, 0, 0));
		g2D.setStroke(new BasicStroke(3.0f));
		g.drawRect(1 + x_tmp, 1, M_block_w - x_tmp * 2, R_block_w - 3);
	}

	// max 255 x 5
	// 顏色轉換
	static int colorcode(int int_color) {
		// int_color+=300; //輸入0 ～1200 回傳 300～1500

		// 0 0 <255
		if (int_color <= 255)
			return int_color;
		// 0 <255 255
		if (int_color <= 255 * 2)
			return (int_color - 255) * 1000 + 255;
		// 0 255 255>
		if (int_color <= 255 * 3)
			return 255000 + 255 - (int_color - 255 * 2);
		// <255 255 0
		if (int_color <= 255 * 4)
			return (int_color - 255 * 3) * 1000000 + 255000;
		// 255 255> 0
		if (int_color <= 255 * 5)
			return 255000000 + (255 - (int_color - 255 * 4)) * 1000;
		// 255> 0 0
		if (int_color <= 255 * 6)
			return (255 - (int_color - 255 * 5)) * 1000000;

		return 0;
	}

	// 顏色轉換
	static int ColorDeploy(double double_color) {
		// 輸入Color_Limit ～ Color_Caps
		// 獲得0 ～ 1200
		if (double_color > Color_Caps)
			double_color = Color_Caps;
		if (double_color < Color_Limit)
			double_color = Color_Limit;

		double Color_Gap = Color_Caps - Color_Limit;
		double Color_Amount, Color_temp;

		int temp = 0;
		for (int i = 0; i < 6; i++)
			temp += Color_Proportion[i];

		Color_Amount = double_color - Color_Limit;
		Color_Amount = Color_Amount / Color_Gap * temp; // 0~100

		Color_temp = 0;
		int temp_i;
		for (temp_i = 0; temp_i < 5; temp_i++)
			if (Color_Amount >= Color_Proportion[temp_i]) {
				Color_Amount -= Color_Proportion[temp_i];
				Color_temp += Color_P_Code[temp_i];
			} else {
				break;
			}

		// 限制顏色上限
		if (Color_Amount > Color_Proportion[temp_i])
			Color_Amount = Color_Proportion[temp_i];
		Color_temp += Color_Amount / Color_Proportion[temp_i] * Color_P_Code[temp_i];

		return colorcode((int) Color_temp + 300);
	}

	// 刷新畫面
	void Reset_Panel() {

		Resolution = 0.01;
		Resolution_flag = 0;
		Resolution_count = 0;
		Pause = 1;
		Player.Button_Start.setIcon(T[2]);

		Postfix[0] = 0;
		Answer_Change = 1;

	}

	// 改變世代數
	void Generation_Change() {
		if (JFrame_mod == 2)
			return;
		if (Now_Generation < 1)
			Now_Generation = 1;
		if (Now_Generation > Total_Generation)
			Now_Generation = Total_Generation;

		Player.Slider_Generation.setValue(Now_Generation);

		Show.Answer_Change = 1;
		Show.Progress_Change = 1;

		Show.Round = 0;
		int_Generation_Change = 0;
		int j = 1, i = 0;

	}

	// 滑鼠判斷
	public MouseAdapter MyMouseAdapter = new MouseAdapter() {
		// ---------------------------------------------------------------------------------------------//
		// 進入
		public void mouseEntered(MouseEvent e) {

			if (e.getSource() == Label_Index) {
				Label_Index.setIcon(T[39 + Int_Index]);
			}
		}

		// 離開
		public void mouseExited(MouseEvent e) {
			if (e.getX() < 0 || e.getX() > 224 || e.getY() < 0 || e.getY() > 35 || e.getSource() == Info_ToggleButton) {
				Label_Index.setIcon(T[38 + Int_Index]);
			}
		}

		// 按下
		public void mousePressed(MouseEvent e) {

			MouseMod = 1;
			if (e.getSource() != Scan_Generation) {
				Scan_Generation.setVisible(false);
				Button_Confirm.setVisible(false);
			}
			if (e.getSource() == GoTo_Confirm) {
				int_Generation_Change = 1;
				try {
//					System.out.println("This");
					Now_Generation = Integer.parseInt(GoTo_Generation.getText());
					if (Now_Generation < 1)
						Now_Generation = 1;
					if (Now_Generation > Total_Generation)
						Now_Generation = Total_Generation;
					GoTo_Generation.setVisible(false);
					GoTo_Confirm.setVisible(false);
					GoTo_Button.setVisible(true);
				} catch (Exception event) {
					int_Generation_Change = 0;
				}
				GoTo_Generation.setVisible(false);
				GoTo_Confirm.setVisible(false);
				GoTo_Button.setVisible(true);
			}
			if (e.getSource() == Button_Confirm) {
				int_Generation_Change = 1;
				try {
//					System.out.println("This");
					Now_Generation = Integer.parseInt(Scan_Generation.getText());
					if (Now_Generation < 1)
						Now_Generation = 1;
					if (Now_Generation > Total_Generation)
						Now_Generation = Total_Generation;
				} catch (Exception event) {
					int_Generation_Change = 0;
				}
			}
			if (e.getSource() == Button_Facebook) {
				try {
					Runtime.getRuntime().exec("cmd /c start https://www.facebook.com/NCNU.114.EPanel");
				} catch (java.io.IOException exception) {
				}
			}
			if (e.getSource() == Button_Stop) {
				frame_Player.Reset_Panel();
				Button_Pause.setIcon(T[2]);
			}
			if (e.getSource() == Button_Pause) {
				// MouseMod = 1;
//				System.out.println("Tol_Gen="+Show.Total_Generation);
				int_Generation_Change = 0;
				if (Pause == 1) {
					Pause = 0;
					frame_Player.Button_Start.setIcon(Show.T[3]);
					Button_Pause.setIcon(T[3]);
					if (Now_Generation == Total_Generation) {
						int_Generation_Change = 1;
						Now_Generation = 1;
						Speed = 16;
						frame_Player.int_Speed = (int) Math.sqrt(Speed);
						frame_Player.Text_Speed.setText("" + frame_Player.int_Speed);
						int_Speed = (int) Math.sqrt(Speed);
						Text_Speed.setText("" + (int_Speed / 4));
					}
				} else {
					Pause = 1;
					frame_Player.Button_Start.setIcon(Show.T[2]);
					Button_Pause.setIcon(Show.T[2]);
				}
			}
			if (e.getSource() == Button_Accelerate) {
				Speed *= 4;
				if (Speed > 4096)
					Speed = 16;
				int_Speed = (int) Math.sqrt(Speed);
				Text_Speed.setText("" + (int_Speed / 4));
				frame_Player.int_Speed = (int) Math.sqrt(Speed);
				frame_Player.Text_Speed.setText("" + Player.int_Speed);
			}
			if (e.getSource() == Button_Decelerate) {
				Speed /= 4;
				if (Speed < 16)
					Speed = 4096;
				int_Speed = (int) Math.sqrt(Speed);
				Text_Speed.setText("" + (int_Speed / 4));
				frame_Player.int_Speed = (int) Math.sqrt(Speed);
				frame_Player.Text_Speed.setText("" + Player.int_Speed);
			}
			if (e.getSource() == Info_ToggleButton) {
				if (Info_pick == 0)
					Info_pick = 1;
				else if (Info_pick == 1)
					Info_pick = 0;
				Answer_Change = 1;

			}
			if (e.getSource() == Converse_Button) {
				
				Show.frame_Converse.setVisible(true);
				Converse.Draw_chart();
			}

			// 透明軸
			if (e.getSource() instanceof JSlider) {

				JSlider jSlider = (JSlider) e.getSource();
				jSlider.setValue((int) (e.getX() * (jSlider.getMaximum() - 1) / jSlider.getWidth()) + 1);
				Answer_Change = 1;
			}

			Answer_JLabel.requestFocus();

			if (e.getSource() == Progress_JLabel) {
				// 進度軸右鍵
				if (e.getButton() == MouseEvent.BUTTON3) {
					int getx = e.getXOnScreen();
					if (getx > 650)
						getx = 650;
					Scan_Generation.setLocation(getx, M_block_y + M_block_l + 14);
					Scan_Generation.setVisible(true);
					Scan_Generation.setText("");
					Button_Confirm.setLocation(getx + 100, M_block_y + M_block_l + 14);
					Button_Confirm.setVisible(true);
					Scan_Generation.requestFocus();
				} else {
					Button_Confirm.setVisible(false);
					Scan_Generation.setVisible(false);
					MouseMod = 3;
					Now_Generation = (int) (e.getX() * Total_Generation / Progress_JLabel.getWidth() + 1);
					Generation_Change();
				}
			}

			if (e.getSource() == GoTo_Button) {
				// for player
				GoTo_Button.setVisible(false);
				GoTo_Generation.setVisible(true);
				GoTo_Generation.setText("");
				GoTo_Confirm.setVisible(true);
				GoTo_Generation.requestFocus();

				// for bar
				/*
				 * Scan_Generation.setLocation(650,M_block_y+M_block_l+18);
				 * Scan_Generation.setVisible(true); Scan_Generation.setText("");
				 * Button_Confirm.setLocation(750,M_block_y+M_block_l+18);
				 * Button_Confirm.setVisible(true); Scan_Generation.requestFocus();
				 */
			}

			if (System_Mode == 1) {
				if (e.getSource() == Button_Borderchange) {
					// 計算視窗中畫布範圍與位置
					Border_X_S = (Border_X_M - Border_X_N) / 2;
					Border_Y_S = (Border_Y_M - Border_Y_N) / 2;

					if (Border_X_S >= Border_Y_S)
						Border_L = Border_X_S;
					else
						Border_L = Border_Y_S;
					if (Dimension_Mode == 1) {
						Border_L = Border_X_S;
					}

					Border_X = Show.Border_X_N + Border_X_S;
					Border_Y = Border_Y_N + Border_Y_S;

					// 重製解空間顏色
					Resolution = 0.01;
					if (Dimension_Mode == 1) {
						if (Border_Y_L == Border_L)
							Border_Y_L = Border_Y_S;
						else
							Border_Y_L = Border_L;
					}
					Answer_Change = 1;
				}
				if (e.getSource() == Button_Video) {
					if (Video_mode == 0) {
						Button_Video.setIcon(Show.T[31]);
						Video_x1 = getX();
						Video_x2 = Outline_x + 20;
						Video_y1 = getY();
						Video_y2 = Outline_y + 160;

						SimpleDateFormat sdFormat = new SimpleDateFormat("yyyyMMdd_HHmmss_SSS");
						Date current = new Date();

						Video_mode = 1;
						try {
							Video_out_file = new AVIOutputStream(
									new File("EPanelcam_" + sdFormat.format(current) + ".avi"),
									AVIOutputStream.VideoFormat.JPG);
							Video_out_file.setVideoCompressionQuality(0.5f);
							Video_out_file.setTimeScale(1);
							Video_out_file.setFrameRate(20);
						} catch (Exception es) {
							es.printStackTrace();
						}
						new Threading().start();
					} else {
						// Button_Video.setIcon(Show.T[30]);
						// Threading().stop();
						Video_mode = 0;
					}
				}
				if (e.getSource() == Button_Camera)
					Screen(getX(), getY(), Outline_x + 20, Outline_y + 160);

				// 按下讀檔
				if (e.getSource() == Button_ReadFile) {

					Show.Pause = 1;
					Show.Resolution = 0.01;
					Resolution_flag = 0;
					Button_Pause.setIcon(Show.T[2]);
					frame_Player.Button_Start.setIcon(Show.T[2]);
					// 開啟舊檔
					JFileChooser chooser = new JFileChooser();
					chooser.setCurrentDirectory(new java.io.File("."));
					chooser.setFileFilter(new FileNameExtensionFilter("EPin", "epin"));

					if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
						Pause = 1;
						Show_Title = 0;
						Progress_Change = 1;
						JFrame_mod = 1;
						Input_Color_Caps.setText("100");
						Input_Color_Limit.setText("0");
						frame_Player.SRF = chooser.getSelectedFile().getName();
						frame_Player.SRF_Name = frame_Player.SRF.substring(0, frame_Player.SRF.lastIndexOf("."));
						frame_Player.Text_ReadFile.setText("" + frame_Player.SRF_Name);
						frame_Player.Reader_File("" + chooser.getSelectedFile());
						frame_Player.Reset_Panel();
					}
					if (Dimension_Mode == 1) {
						Button_Borderchange.setVisible(true);
						Bottom_JPanel.setSize(370, 32);
					} else {
						Button_Borderchange.setVisible(false);
						Bottom_JPanel.setSize(370, 32);
					}
					Label_Index.setLocation(356 - Bottom_JPanel.getWidth() / 2, Label_Index.getY());
					Bottom_JPanel.setLocation(356 - Bottom_JPanel.getWidth() / 2 + 32, Label_Index.getY());
				}

				if (e.getSource() == Input_Color_Caps)
					Input_Color_Caps.setEditable(true);
				else
					Input_Color_Caps.setEditable(false);

				if (e.getSource() == Input_Color_Limit)
					Input_Color_Limit.setEditable(true);
				else
					Input_Color_Limit.setEditable(false);

				if (e.getSource() == Zoom_ToggleButton) {
					Answer_Change = 1;
					// Communication_pick=(Communication_pick+1)%3;
					if (Zoom_pick == 0) {
						Zoom_pick = 1;
						Zoom_ToggleButton.setIcon(Show.T[25]);
						frame_Player.Eraser_pick = 1;
						frame_Player.Eraser_ToggleButton.setIcon(Show.T[25]);

					} else if (Zoom_pick == 1) {
						Zoom_pick = 0;
						Zoom_ToggleButton.setIcon(Show.T[24]);
						frame_Player.Eraser_pick = 0;
						frame_Player.Eraser_ToggleButton.setIcon(Show.T[24]);
					}
				}

				if (e.getSource() == Probability_ToggleButton) {
					if (Show_P == 0)
						Show_P = 1;
					else if (Show_P == 1)
						Show_P = 0;
					Answer_Change = 1;

				}

				// Input_Color_Caps.setEditable(false);
				/*
				 * //視窗按鈕 JButton Button_Player=new JButton(); JButton Button_Detection=new
				 * JButton();
				 */

			} else if (System_Mode == 2) {
				if (e.getSource() == Button_Video) {
					if (Video_mode == 0) {
						Button_Video.setIcon(Show.T[31]);
						Video_x1 = getX();
						Video_x2 = Outline_y + 20;
						Video_y1 = getY();
						Video_y2 = Outline_y + 160;

						SimpleDateFormat sdFormat = new SimpleDateFormat("yyyyMMdd_HHmmss_SSS");
						Date current = new Date();

						Video_mode = 1;
						try {
							Video_out_file = new AVIOutputStream(
									new File("EPanelcam_" + sdFormat.format(current) + ".avi"),
									AVIOutputStream.VideoFormat.JPG);
							Video_out_file.setVideoCompressionQuality(0.5f);
							Video_out_file.setTimeScale(1);
							Video_out_file.setFrameRate(20);
						} catch (Exception es) {
							es.printStackTrace();
						}
						new Threading().start();
					} else {
						// Button_Video.setIcon(Show.T[30]);
						// Threading().stop();
						Video_mode = 0;
					}
				}
				if (e.getSource() == Button_Camera)
					Screen(getX(), getY(), Outline_y + 20, Outline_y + 160);

				if (e.getSource() == Button_ReadFile) {

					Show.Pause = 1;
					Button_Pause.setIcon(Show.T[2]);
					frame_Player.Button_Start.setIcon(Show.T[2]);
					// 開啟舊檔
					JFileChooser chooser = new JFileChooser();
					chooser.setCurrentDirectory(new java.io.File("."));
					chooser.setFileFilter(new FileNameExtensionFilter("EPin", "epin"));

					if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
						Pause = 1;
						Show_Title = 0;
						Progress_Change = 1;
						JFrame_mod = 1;
						frame_Player.SRF = chooser.getSelectedFile().getName();
						frame_Player.SRF_Name = frame_Player.SRF.substring(0, frame_Player.SRF.lastIndexOf("."));
						frame_Player.Text_ReadFile.setText("" + frame_Player.SRF_Name);
						frame_Player.Reader_File("" + chooser.getSelectedFile());
						frame_Player.Reset_Panel();
					}
					Label_Index.setLocation(356 - Bottom_JPanel.getWidth() / 2, Label_Index.getY());
					Bottom_JPanel.setLocation(356 - Bottom_JPanel.getWidth() / 2 + 32, Label_Index.getY());
				}
				if (e.getSource() == Probability_ToggleButton) {
					if (Now_Generation == Total_Generation) {
						if (Show_P == 1)
							Show_P = 0;
						else
							Show_P = 1;

						System.out.println("Show_P =" + Show_P);

					}
				}
			}
		}// 按下END
			// 放開滑鼠

		public void mouseReleased(MouseEvent e) {

			if (System_Mode == 1) {
				// 透明軸
				if (e.getSource() instanceof JSlider) {

					JSlider jSlider = (JSlider) e.getSource();
					jSlider.setValue((int) (e.getX() * (jSlider.getMaximum() - 1) / jSlider.getWidth()) + 1);
					Answer_Change = 1;
				}

				if (e.getSource() == Label_Index) {
					Label_temp.setVisible(false);
					Label_Index.setVisible(true);
					if (Int_Index == 2) {
						Bottom_JPanel.setVisible(true);
					}

					if (e.getX() >= 0 && e.getX() <= 32) {
						Index_x2 = 0;
					} else {
						Index_x2 = e.getX();
					}
					Index_x1 = Label_Index.getX() + Index_x2 - 16;
					if (Index_x2 != 0) {
						if (Index_x1 > 3 && Index_x1 < 518) {
							Label_Index.setLocation(Index_x1, Label_Index.getY());
							Bottom_JPanel.setLocation(Index_x1 + 32, Label_Index.getY());
						} else if (Index_x1 <= 3) {
							Label_Index.setLocation(4, Label_Index.getY());
							Bottom_JPanel.setLocation(4 + 32, Label_Index.getY());
						} else {
							Label_Index.setLocation(806 - Bottom_JPanel.getWidth(), Label_Index.getY());
							Bottom_JPanel.setLocation(806 - Bottom_JPanel.getWidth() + 32, Label_Index.getY());
						}
						// System.out.println("x = "+Label_Index.getX()+" x1 = "+x1+" x2 = "+x2);
					} else {
						if (Int_Index == 0) {
							Int_Index = 2;
							Label_Index.setIcon(T[39 + Int_Index]);
							Bottom_JPanel.setVisible(true);
						} else {
							Int_Index = 0;
							Label_Index.setIcon(T[39 + Int_Index]);
							Bottom_JPanel.setVisible(false);
						}
					}
					Index_x2 = 0;
				}

				if (MouseMod == 0) {
					Answer_Change = 1;
				}
			} else if (System_Mode == 2) {

			}
		}

		// 點擊滑鼠
		public void mouseClicked(MouseEvent e) {
			PFitness_JLabel.setVisible(false);
			if (System_Mode == 1) {
				if (e.getSource() == Answer_JLabel) {
					// 粒子資訊
//					System.out.println("x ="+e.getX());
//					System.out.println("y ="+e.getY());
//					System.out.println(Draw_Generation);
					int i;
					float j = 0;
					float k = 0;
					int now_bit = 0;
					int best = 0;
					int bit_num = Generation[Now_Generation].X[0].Amount;
					float bit_l = 0;
					float bit_w = 0;

					if (Zoom_pick == 0) {
						bit_l = (float) M_block_l / (float) Particle_shownum;
						i = Draw_Generation - 1;
					} else {
						bit_l = (float) M_block_l / (float) Generation[Now_Generation].Particle;
						i = -1;
					}

					for (j = 0; j <= e.getY(); j += bit_l) {
						i++;
						if (i > Now_Generation && Zoom_pick == 0)
							i = Generation[Now_Generation].GBest_gen;
						if (Zoom_pick == 0) {
							best = Generation[i].CBest;
							bit_num = Generation[i].X[best].Amount;
						}
						bit_w = (float) M_block_w / (float) bit_num;
						now_bit = -1;
						for (k = 0; k <= e.getX(); k += bit_w) {
							now_bit++;
						}
					}
					// j -= bit_l;
					k -= bit_w;
					if (Zoom_pick == 1) {
						best = i;
						i = Now_Generation;
					}

//					System.out.println("g ="+i);
//					System.out.println("p ="+best);
//					System.out.println("b ="+now_bit);
					PFitness_JLabel.setBounds((int) k, (int) j + 35, 100, 60);
					PFitness_JLabel.setText("<html><div style='text-align: center;'>"
							+ Generation[i].X[best].BitInfo[now_bit] + "<br>" + Generation[i].X[best].Bit[now_bit]
							+ "  " + Generation[i].X[best].Probability[now_bit] + "</div></html> ");
					PFitness_JLabel.setVisible(true);

				}
				// 透明軸點擊
				if (e.getSource() instanceof JSlider) {

					JSlider jSlider = (JSlider) e.getSource();
					jSlider.setValue((int) (e.getX() * (jSlider.getMaximum() - 1) / jSlider.getWidth()) + 1);
					Answer_Change = 1;
				}
			} else if (System_Mode == 2) {
				if (e.getSource() == Answer_JLabel) {
					int row = -1;
					int col = -1;
					float j = 0;
					float k = 0;
					float sensor_w = (float) M_block_l / Map[Now_Generation].col;
					float sensor_l = (float) M_block_l / Map[Now_Generation].row;
					DecimalFormat df0 = new DecimalFormat("#0.000000");

					for (j = 0; j <= e.getY(); j += sensor_l) {
						row = -1;
						for (k = 0; k <= e.getX(); k += sensor_w) {
							row++;
						}
						col++;
					}
					k -= sensor_w;
					if (Show_P == 0) {
						PFitness_JLabel.setBounds((int) (k + 35), (int) (j + (sensor_l * 0.75)), 100, 60);
						PFitness_JLabel.setText("<html><div style='text-align: center;'>"
								+ df0.format(Map[Now_Generation].Probability[col][row]) + "<br> Limit :"
								+ Map[Now_Generation].limit[row][col] + "</div></html> ");
						PFitness_JLabel.setVisible(true);
					} else {
						if (e.getButton() == MouseEvent.BUTTON3) {
							System.out.println("p_press");
							if (temp_Map.Sensor[col][row] == 1)
								temp_Map.Sensor[col][row] = 0;
							else
								temp_Map.Sensor[col][row] = 1;

							double temp = 0;
							for (int c = col - 6; (c < col + 6 && c < temp_Map.col); c++) {
								if (c < 0)
									c = 0;
								for (int r = row - 6; (r < row + 6 && r < temp_Map.row); r++) {
									if (r < 0)
										r = 0;
									if (temp_Map.Sensor[c][r] == 1) {
										temp_Map.Probability[c][r] = -1;
										System.out.println("c =" + c);
										System.out.println("r =" + r);
									} else {
										temp = 1;
										for (int ij = c - 6; ij < c + 6; ij++) {
											for (int ik = r - 6; ik < r + 6; ik++) {
												if (ij >= 0 && ik >= 0 && ij < temp_Map.col && ik < temp_Map.row) {
													if (temp_Map.Sensor[ij][ik] == 1) {
														double distance = Math
																.sqrt((ij - c) * (ij - c) + (ik - r) * (ik - r));
														System.out.println("distance =" + distance);
														temp *= (1 - (double) 1 / distance);
													}
												}
											}
										}
										temp_Map.Probability[c][r] = 1 - temp;
										System.out.println("p =" + temp_Map.Probability[c][r]);
									}

								}
							}

						}
						Answer_Change = 1;

					}

					System.out.println("x(col) =" + col);
					System.out.println("y(row) =" + row);
					System.out.println("x(col) =" + k);
					System.out.println("y(row) =" + j);
				}

			}
		}

		// 移動滑鼠
		public void mouseMoved(MouseEvent e) {
			if (System_Mode == 1 && Show.Fitness_Amount > 0) {
				if (e.getSource() == Answer_JLabel && Fitness_Amount > 0) {
					DecimalFormat df_11 = new DecimalFormat("######0.000000");

					float bit_h = (float) M_block_l / (float) Particle_shownum;
					float Start_y = 0;
					float End_y = bit_h;
					float mouse_y = e.getY();
					int Pos_x = M_block_w / 2 - 120;

					if (Zoom_pick == 0) {
						for (int i = Draw_Generation; i <= Now_Generation; i++) {
							if (mouse_y >= Start_y && mouse_y < End_y) {
								int best = Generation[i].CBest;
								Text_Fitness.setText("f(x)=" + Generation[i].X[best].Fitness);
								/*
								 * PFitness_String = ""+df_11.format(Generation[i].X[best].Fitness);
								 * PFitness_JLabel.setLocation(Pos_x+M_block_x,(int)Start_y+67);
								 * PFitness_JLabel.setText(PFitness_String); PFitness_JLabel.setVisible(true);
								 * // System.out.println("Draw_Gen="+Draw_Generation); //
								 * System.out.println("GFitness="+Generation[i].X[best].Fitness); //
								 * System.out.println("PFitness="+PFitness_String);
								 */ }
							Start_y = End_y;
							End_y += bit_h;
						}
					} else {
						int i = Now_Generation;
						for (int j = 0; j < Generation[i].Particle; j++) {
							if (mouse_y >= Start_y && mouse_y < End_y) {
								Text_Fitness.setText("f(x)=" + Generation[i].X[j].Fitness);
								// System.out.println("f(x)="+Generation[i].X[j].Fitness);
								// System.out.println("j="+j);
							}
							Start_y = End_y;
							End_y += bit_h;
						}
					}
				}
			} else if (System_Mode == 2) {
				if (e.getSource() == Answer_JLabel) {
					int row = -1;
					int col = -1;
					float j = 0;
					float k = 0;
					float sensor_w = (float) M_block_l / Map[Now_Generation].row;
					float sensor_l = (float) M_block_l / Map[Now_Generation].col;
					DecimalFormat df0 = new DecimalFormat("#0.000000");

					for (j = 0; j <= e.getY(); j += sensor_l) {
						col = -1;
						for (k = 0; k <= e.getX(); k += sensor_w) {
							col++;
						}
						row++;
					}
//					System.out.println((row+1));
//					System.out.println((col+1));
//					System.out.println(Map[Now_Generation].Probability[row][col]);
					Text_Fitness.setText(
							"f(" + (row + 1) + "," + (col + 1) + ")=" + Map[Now_Generation].Probability[row][col]);
				}
			}
		}

		// 拖移滑鼠
		public void mouseDragged(MouseEvent e) {
			Scan_Generation.setVisible(false);
			Button_Confirm.setVisible(false);
			GoTo_Generation.setVisible(false);
			GoTo_Confirm.setVisible(false);
			if (System_Mode == 1) {
				if (e.getSource() == Progress_JLabel) {
					Now_Generation = (int) (e.getX() * Total_Generation / Progress_JLabel.getWidth() + 1);
					Generation_Change();
				}
				// 世代軸拖移
				if (e.getSource() instanceof JSlider) {
					JSlider jSlider = (JSlider) e.getSource();
					jSlider.setValue((int) (e.getX() * (jSlider.getMaximum() - 1) / jSlider.getWidth()) + 1);
					Answer_Change = 1;
				}
				if (MouseMod == 1) {
					MouseXago = e.getX();
					MouseYago = e.getY();
					MouseMod = 0;
				}

				if (e.getX() >= R_block_x && MouseMod < 3) {
					// if(e.getY()<Progress_JLabel.getY())
					// System.out.println(""+e.getY());
					Color_Caps -= (double) (e.getY() - MouseYago) * (Color_Caps - Color_Limit) / 350;
					Color_Limit -= (double) (e.getY() - MouseYago) * (Color_Caps - Color_Limit) / 350;

					Input_Color_Limit.setText("" + df.format(Color_Limit));
					Input_Color_Caps.setText("" + df.format(Color_Caps));

					Answer_Change = 1;
					MouseXago = e.getX();
					MouseYago = e.getY();
				}
				if (e.getSource() == Answer_JLabel) {

					Border_X -= (double) (e.getX() - MouseXago) * Border_L / 350;
					if (Dimension_Mode == 2) {
						Border_Y += (double) (e.getY() - MouseYago) * Border_L / 350;
					} else if (Dimension_Mode == 1) {
						Border_Y += (double) (e.getY() - MouseYago) * Border_Y_L / 350;
					}
					Answer_Change = 1;
					MouseXago = e.getX();
					MouseYago = e.getY();
				}
				if (e.getSource() == Label_Index) {
					Label_Index.setVisible(false);
					if (Int_Index == 2) {
						Bottom_JPanel.setVisible(false);
					}

					int x1;

					x1 = Label_Index.getX() + e.getX();

					Label_temp.setVisible(true);
					Label_temp.setSize(32, 32);
					Label_temp.setIcon(T[39 + Int_Index]);
					if (x1 > 3 && x1 < 550) {
						Label_temp.setLocation(Label_Index.getX() + e.getX() - 16, Label_Index.getY());
					} else if (x1 <= 3) {
						Label_temp.setLocation(Label_Index.getX() + e.getX() - 16, Label_Index.getY());
					} else {
						Label_temp.setLocation(Label_Index.getX() + e.getX() - 16, Label_Index.getY());
					}
				}
			} else if (System_Mode == 2) {

			}
		}

		// 滾動滑鼠
		public void mouseWheelMoved(MouseWheelEvent e) {

			MouseMod = 5;
			if (System_Mode == 1) {
				if (e.getSource() == Answer_JLabel) {
					Answer_Change = 1;
					// System.out.println(""+Border_L);
					if (Resolution_count != 0) {
						Resolution /= Math.pow(1.2, Resolution_count);
						Resolution_count = 0;
					}
					if (e.getWheelRotation() > 0)// && Border_L<100000)
					{
						Border_X += (-(double) (e.getX() - 385) * Border_L / 3500);
						if (Dimension_Mode == 2) {
							Border_Y -= (-(double) (e.getY() - 405) * Border_L / 3500);
						} else if (Dimension_Mode == 1) {
							Border_Y -= (-(double) (e.getY() - 405) * Border_Y_L / 3500);
						}
						Border_L *= 1.1;
						Border_Y_L *= 1.1;
						Resolution *= 1.2;
						if (Resolution >= 0.01 * (Math.pow(1.2, 5)) && Resolution_flag == 0) {
							Resolution_flag++;
							Resolution = 0.01 * (Math.pow(1.2, 5));
						}
						if (Resolution_flag != 0) {
							Resolution_flag++;
							Resolution /= 1.2;
						}
					}
					if (e.getWheelRotation() < 0)// && Border_L>0.01)
					{
						Border_X += ((double) (e.getX() - 385) * Border_L / 3500);
						if (Dimension_Mode == 2) {
							Border_Y -= (-(double) (e.getY() - 405) * Border_L / 3500);
						} else if (Dimension_Mode == 1) {
							Border_Y -= (-(double) (e.getY() - 405) * Border_Y_L / 3500);
						}
						Border_L /= 1.1;
						Border_Y_L /= 1.1;
						Resolution /= 1.2;
						if (Resolution <= 0.01 / (Math.pow(1.2, 20)) && Resolution_flag == 0) {
							Resolution_flag--;
							Resolution = 0.01 / (Math.pow(1.2, 20));
						}
						if (Resolution_flag != 0) {
							Resolution_flag--;
							Resolution *= 1.2;
						}
					}

					Answer_Change = 1;

				}
			} else if (System_Mode == 2) {

			}
		}
	};
	// 鍵盤判斷
	public KeyAdapter MyKeyAdapter = new KeyAdapter() {
		// 按下
		public void keyPressed(KeyEvent es) {
			int proportion;
			proportion = (int) Math.log10((double) Total_Generation);
//			if(es.getKeyCode() == KeyEvent.VK_ENTER&&Scan_Generation.isVisible())
			if (es.getKeyCode() == KeyEvent.VK_ENTER && GoTo_Generation.isVisible()) {
				int_Generation_Change = 1;
				try {
					// Now_Generation=Integer.parseInt(Scan_Generation.getText());
					Now_Generation = Integer.parseInt(GoTo_Generation.getText());
					if (Now_Generation < 1)
						Now_Generation = 1;
					if (Now_Generation > Total_Generation)
						Now_Generation = Total_Generation;
				} catch (Exception event) {
					int_Generation_Change = 0;
				}
//				Scan_Generation.setVisible(false);
//				Button_Confirm.setVisible(false);
				GoTo_Generation.setVisible(false);
				GoTo_Confirm.setVisible(false);
				Answer_JLabel.requestFocus();
				GoTo_Button.setVisible(true);
			}
//			if(!Scan_Generation.isVisible())
			switch (es.getKeyCode()) {
			case KeyEvent.VK_DECIMAL: // 小鍵盤的 POINT 主鍵盤的是 VK_PERIOD;
				Now_Generation = Total_Generation;
				Generation_Change();
				break;
			case KeyEvent.VK_ADD:
				if (Dimension_Mode == 2)
					Slider_Transparency.setValue(Slider_Transparency.getValue() + 15);

				if (Resolution_count < 11 && Dimension_Mode == 1) {
					Resolution *= 1.2;
					Resolution_count++;
				}
				Answer_Change = 1;
				break;
			case KeyEvent.VK_SUBTRACT:
				if (Dimension_Mode == 2)
					Slider_Transparency.setValue(Slider_Transparency.getValue() - 15);

				if (Resolution_count > -11 && Dimension_Mode == 1) {
					Resolution /= 1.2;
					Resolution_count--;
				}
				Answer_Change = 1;
				break;
			case KeyEvent.VK_NUMPAD1:
				Now_Generation = Total_Generation / 10;
				Generation_Change();
				break;
			case KeyEvent.VK_NUMPAD2:
				Now_Generation = Total_Generation / 10 * 2;
				Generation_Change();
				break;
			case KeyEvent.VK_NUMPAD3:
				Now_Generation = Total_Generation / 10 * 3;
				Generation_Change();
				break;
			case KeyEvent.VK_NUMPAD4:
				Now_Generation = Total_Generation / 10 * 4;
				Generation_Change();
				break;
			case KeyEvent.VK_NUMPAD5:
				Now_Generation = Total_Generation / 10 * 5;
				Generation_Change();
				break;
			case KeyEvent.VK_NUMPAD6:
				Now_Generation = Total_Generation / 10 * 6;
				Generation_Change();
				break;
			case KeyEvent.VK_NUMPAD7:
				Now_Generation = Total_Generation / 10 * 7;
				Generation_Change();
				break;
			case KeyEvent.VK_NUMPAD8:
				Now_Generation = Total_Generation / 10 * 8;
				Generation_Change();
				break;
			case KeyEvent.VK_NUMPAD9:
				Now_Generation = Total_Generation / 10 * 9;
				Generation_Change();
				break;
			case KeyEvent.VK_NUMPAD0:
				Now_Generation = 1;
				Generation_Change();
				break;
			case KeyEvent.VK_END:
				Now_Generation = Total_Generation;
				Generation_Change();
				break;
			case KeyEvent.VK_UP: // 向上
				Speed *= 4;
				if (Speed > 4096)
					Speed = 16;
				frame_Player.int_Speed = (int) Math.sqrt(Speed);
				frame_Player.Text_Speed.setText("" + Player.int_Speed);
				int_Speed = (int) Math.sqrt(Speed);
				Text_Speed.setText("" + (int_Speed / 4));
				break;
			case KeyEvent.VK_DOWN: // 向下
				Speed /= 4;
				if (Speed < 16)
					Speed = 4096;
				frame_Player.int_Speed = (int) Math.sqrt(Speed);
				frame_Player.Text_Speed.setText("" + Player.int_Speed);
				int_Speed = (int) Math.sqrt(Speed);
				Text_Speed.setText("" + (int_Speed / 4));
				break;
			case KeyEvent.VK_LEFT: // 向左
//				Now_Generation -= Total_Generation / Math.pow(10.0, proportion);
				Now_Generation -= 1;
				Generation_Change();
				break;
			case KeyEvent.VK_RIGHT: // 向右
//				Now_Generation += Total_Generation / Math.pow(10.0, proportion);
				Now_Generation += 1;
				Generation_Change();
				break;
			case KeyEvent.VK_SPACE: // 空白
				if (Pause == 1) {
					Pause = 0;
					frame_Player.Button_Start.setIcon(Show.T[3]);
					Button_Pause.setIcon(T[3]);
					if (Now_Generation == Total_Generation) {
						int_Generation_Change = 1;
						Now_Generation = 1;
						Speed = 16;
						frame_Player.int_Speed = (int) Math.sqrt(Speed);
						frame_Player.Text_Speed.setText("" + frame_Player.int_Speed);
						int_Speed = (int) Math.sqrt(Speed);
						Text_Speed.setText("" + (int_Speed / 4));
					}
				} else {
					Pause = 1;
					frame_Player.Button_Start.setIcon(Show.T[2]);
					Button_Pause.setIcon(Show.T[2]);
				}
			}
		}
	};// KEY-END
		// 視窗判斷
	public WindowAdapter MyWindowListener = new WindowAdapter() {

		// System.out.println("焦點");
		public void windowActivated(WindowEvent e) {
		}

		// System.out.println("失焦");
		public void windowDeactivated(WindowEvent e) {
		}

		public void windowClosing(WindowEvent e) {
			Video_mode = 0;
		}

		public void windowClosed(WindowEvent e) {

			// System.exit( -1 );
		}
	};

}// Show END

//播放控制器
class Player extends EPanel implements ChangeListener {

	static JLabel Text_Speed = new JLabel("1");
	JLabel Label_Speed = new JLabel();
	static int int_Speed = 1;

	JButton Button_Stop = new JButton();
	static JButton Button_Start = new JButton();
	// JButton Button_Pause=new JButton();
	JButton Button_Accelerate = new JButton();
	JButton Button_ReadFile = new JButton();

	JButton Button_Auto = new JButton();
	JButton Button_Recovery = new JButton();

	JButton Button_Decelerate = new JButton();

	// 世代數控調
	static JSlider Slider_Generation;
	static float float_Generation = 0;

	static JTextField Scan_Generation = new JTextField();
	static JButton Button_Confirm = new JButton();
	static JLabel Text_Generation = new JLabel("1/1");
	JPanel Panel_Generation = new JPanel();

	// 「橡皮擦」選項
	JToggleButton Eraser_ToggleButton = new JToggleButton();
	static int Eraser_pick = 0;
	/*
	 * static JCheckBox Eraser_Box = new JCheckBox(); static JCheckBox
	 * Eraser_Color_Box = new JCheckBox();
	 */

	// 讀重疊設定
	static JToggleButton Overlapping_ToggleButton = new JToggleButton();

	// 讀檔名稱
	JLabel Text_ReadFile = new JLabel("");
	String SRF;
	static File Player_file;
	static String SRF_Name;

	// 最佳解
	static JLabel Text_Fitness = new JLabel("0.0");
	JPanel Panel_Fitness = new JPanel();

	// 方程式
	JTextField Input_Formula = new JTextField("0");

	// ------------------------------------------------------------------主視窗程式碼
	Player()// throws Exception
	{
		// 讀檔
		Button_ReadFile = new JButton(Show.T[5]);
		// Button_ReadFile.setToolTipText("開啟檔案EPin");
		Button_ReadFile.setMargin(new Insets(0, 0, 0, 0)); // 設置邊框和標籤的間距
		Button_ReadFile.setBounds(4, 4, 32, 32);
		Button_ReadFile.addMouseListener(MyMouseAdapter);
		add(Button_ReadFile);
		// 讀重疊設定
		Overlapping_ToggleButton = new JToggleButton(Show.T[16]);
		// Overlapping_ToggleButton.setToolTipText("重疊讀檔");
		Overlapping_ToggleButton.setMargin(new Insets(0, 0, 0, 0)); // 設置邊框和標籤的間距
		Overlapping_ToggleButton.addMouseListener(MyMouseAdapter);
		Overlapping_ToggleButton.setBounds(36, 4, 32, 32);
		add(Overlapping_ToggleButton);

		// 讀檔檔案名稱
		// Text_ReadFile.setToolTipText("檔案名稱");
		Text_ReadFile.setBounds(68, 4, 150, 32);
		Text_ReadFile.setFont(new Font("微軟正黑體", Font.BOLD, 18));
		Text_ReadFile.setHorizontalAlignment(JTextField.CENTER);
		Text_ReadFile.setBorder(BorderFactory.createEtchedBorder());
		add(Text_ReadFile);

		// 世代數外框
		Panel_Generation.setBounds(220, 1, 147, 38);
		// Panel_Generation.setBorder(new TitledBorder("世代數"));
		Panel_Generation.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "世代數"));
		// Panel_Generation.setBackground(new Color(250,250,250));
		Panel_Generation.setLayout(null);
		add(Panel_Generation);

		// 世代數輸入
		Scan_Generation.setBounds(20, 12, 80, 22);
		Scan_Generation.setVisible(false);
		Scan_Generation.setFont(new Font("微軟正黑體", Font.BOLD, 16));
		Panel_Generation.add(Scan_Generation);

		Button_Confirm.setBounds(100, 12, 22, 22);
		Button_Confirm.setIcon(Show.T[36]);
		Button_Confirm.setFont(new Font("微軟正黑體", Font.BOLD, 12));
		Button_Confirm.setVisible(false);
		Button_Confirm.addMouseListener(MyMouseAdapter);
		Panel_Generation.add(Button_Confirm);

		// 世代數
		// Text_Generation.setToolTipText("當前世代數/總世代數");
		Text_Generation.setBounds(0, 12, 147, 20);
		Text_Generation.setFont(new Font("微軟正黑體", Font.BOLD, 18));
		Text_Generation.setHorizontalAlignment(JLabel.CENTER);
		Text_Generation.addMouseListener(MyMouseAdapter);
		/*
		 * Text_Generation.setBorder(BorderFactory.createTitledBorder(
		 * BorderFactory.createEtchedBorder(),"Slider 2",TitledBorder.LEFT,
		 * TitledBorder.TOP));
		 */
		Panel_Generation.add(Text_Generation);

		// 世代數控制條
		Slider_Generation = new JSlider(0, 0, 0);
		// Slider_Generation.setToolTipText("世代數播放控制條");
		Slider_Generation.setBounds(4, 46, 364, 30);
		// Slider_Generation.setBackground(new Color(255,255,255));
		// Slider_Generation.setPaintTicks(true);//
		// setPaintTicks()方法是設置是否在JSlider加上刻度，若為true則下面兩行才有作用。
		// Slider_Generation.setMajorTickSpacing(100);
		// Slider_Generation.setMinorTickSpacing(100);
		// Slider_Generation.setSnapToTicks(true);//
		// setSnapToTicks()方法表示一次移動一個小刻度，而不再是一次移動一個單位刻度。
		// Slider_Generation.setPaintLabels(true);
		// Slider_Generation.setPaintTrack(false);//setPaintTrack()方法表示是否出現滑動桿的橫桿。默認值為true.
		// Slider_Generation.putClientProperty("JSlider.isFilled", Boolean.TRUE);
		Slider_Generation.setMinimum(1);
		Slider_Generation.addChangeListener(this);
		Slider_Generation.addMouseListener(MyMouseAdapter);
		Slider_Generation.addMouseMotionListener(MyMouseAdapter);
		Slider_Generation.setPaintTicks(true); // 指針狀

		add(Slider_Generation);

		// 停止
		Button_Stop = new JButton(Show.T[1]);
		Button_Stop.setToolTipText("Stop");
		Button_Stop.setMargin(new Insets(0, 0, 0, 0)); // 設置邊框和標籤的間距
		Button_Stop.setBounds(4, 78, 32, 32);
		Button_Stop.addMouseListener(MyMouseAdapter);
		add(Button_Stop);
		// 開始
		Button_Start = new JButton(Show.T[2]);
		Button_Start.setToolTipText("Play");
		Button_Start.setMargin(new Insets(0, 0, 0, 0)); // 設置邊框和標籤的間距
		Button_Start.setBounds(36, 78, 32, 32);
		Button_Start.addMouseListener(MyMouseAdapter);
		add(Button_Start);
		// 反轉
		Button_Decelerate = new JButton(Show.T[37]);
		Button_Decelerate.setToolTipText("Decelerate");
		Button_Decelerate.setMargin(new Insets(0, 0, 0, 0)); // 設置邊框和標籤的間距
		Button_Decelerate.setBounds(68, 78, 32, 32);
		Button_Decelerate.addMouseListener(MyMouseAdapter);
		add(Button_Decelerate);
		// 快轉
		Button_Accelerate = new JButton(Show.T[4]);
		Button_Accelerate.setToolTipText("Accelerate");
		Button_Accelerate.setMargin(new Insets(0, 0, 0, 0)); // 設置邊框和標籤的間距
		Button_Accelerate.setBounds(100, 78, 32, 32);
		Button_Accelerate.addMouseListener(MyMouseAdapter);
		add(Button_Accelerate);
		// 速度
		// Text_Speed.setToolTipText("目前播放倍率");
		Text_Speed.setBounds(132, 78, 32, 32);
		Text_Speed.setFont(new Font("微軟正黑體", Font.BOLD, 14));
		// Text_Speed.setBackground(new Color(255,000,0));
		Text_Speed.setOpaque(false);
		Text_Speed.setHorizontalAlignment(JLabel.CENTER);
		add(Text_Speed);
		// 速度底圖
		Label_Speed = new JLabel(Show.T[9]);
		Label_Speed.setBounds(132, 78, 32, 32);
		// Text_Speed.setBackground(new Color(255,000,0));
		// Label_Speed.setOpaque(false);
		add(Label_Speed);

		// 橡皮擦選項按鈕
		Eraser_ToggleButton = new JToggleButton(Show.T[24]);
		Eraser_ToggleButton.setToolTipText("History");
		Eraser_ToggleButton.setMargin(new Insets(0, 0, 0, 0)); // 設置邊框和標籤的間距
		Eraser_ToggleButton.addMouseListener(MyMouseAdapter);
		Eraser_ToggleButton.setBounds(164, 78, 32, 32);
		add(Eraser_ToggleButton);
		/*
		 * //橡皮擦勾選 Eraser_Box.setToolTipText("顯示粒子停留過的點"); Eraser_Box.setText("記錄");
		 * Eraser_Box.setFont(new Font("微軟正黑體",Font.BOLD,12));
		 * Eraser_Box.setBounds(268,80,49,15); add(Eraser_Box); //橡皮擦顏色勾選
		 * Eraser_Color_Box.setToolTipText("顏色顯示粒子停留過的點");
		 * Eraser_Color_Box.setText("顏色"); Eraser_Color_Box.setFont(new
		 * Font("微軟正黑體",Font.BOLD,12)); Eraser_Color_Box.setBounds(268,95,49,15);
		 * add(Eraser_Color_Box);
		 */

		// 自動播放
		Button_Auto = new JButton(Show.T[27]);
		// Button_Auto.setToolTipText("依照檔案自動暫停、延遲、加減速");
		Button_Auto.setMargin(new Insets(0, 0, 0, 0)); // 設置邊框和標籤的間距
		Button_Auto.setBounds(228, 77, 34, 34);
		Button_Auto.addMouseListener(MyMouseAdapter);
		// add(Button_Auto);

		Button_Recovery = new JButton(Show.T[12]);
		Button_Recovery.setToolTipText("Default");
		Button_Recovery.setMargin(new Insets(0, 0, 0, 0)); // 設置邊框和標籤的間距
		Button_Recovery.setBounds(196, 77, 34, 34);
		Button_Recovery.addMouseListener(MyMouseAdapter);
		add(Button_Recovery);

		// 最佳解外框
		Panel_Fitness.setBounds(232, 75, 135, 38);
		Panel_Fitness.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Fitness"));
		// Panel_Generation.setBackground(new Color(250,250,250));
		Panel_Fitness.setLayout(null);
		add(Panel_Fitness);

		// 最佳解
		// Text_Fitness.setToolTipText("當前世代找到的最好解");
		Text_Fitness.setBounds(0, 12, 400, 20);
		Text_Fitness.setFont(new Font("微軟正黑體", Font.BOLD, 18));
		Text_Fitness.setHorizontalAlignment(JLabel.CENTER);
		Panel_Fitness.add(Text_Fitness);

		Input_Formula.setBounds(5, 120, 364, 32);
		// Input_Formula.setToolTipText("方程式顯示框");
		Input_Formula.setFont(new Font("微軟正黑體", Font.BOLD, 14));
		Input_Formula.setHorizontalAlignment(JTextField.CENTER);
		Input_Formula.setBorder(BorderFactory.createEtchedBorder());
		add(Input_Formula);
		/*
		 * //停止 Button_Stop= new JButton(T[1]); Button_Stop.setMargin(new Insets(0, 0,
		 * 0, 0)); //設置邊框和標籤的間距 Button_Stop.setBounds(20,25,32,32);
		 * Button_Stop.addMouseListener(check); Panel_Generation.add(Button_Stop); //暫停
		 * Button_Pause= new JButton(T[0]); Button_Pause.setMargin(new Insets(0, 0, 0,
		 * 0)); //設置邊框和標籤的間距 Button_Pause.setBounds(140,75,32,32);
		 * Button_Pause.addMouseListener(check); Button_Pause.setOpaque(false);
		 * add(Button_Pause);
		 */

		Text_ReadFile.setDropTarget(new DropTarget() {
			public void drop(DropTargetDropEvent e) {
				e.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
				try {
					List droppedFiles = (List) e.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
					File file = (File) droppedFiles.get(0);
					System.out.print(file.getAbsolutePath());

					Reader_File("" + file.getAbsolutePath());
					SRF = file.getName();
					SRF_Name = SRF.substring(0, SRF.lastIndexOf("."));

					Text_ReadFile.setText("" + SRF_Name);

					Reset_Panel();

				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		setIconImage(Show.T[11].getImage()); // 圖標設定
		setLayout(null);
		setTitle("演化計算播放器  " + Show.Version);
		setBounds(855, 160, 392, 200);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(MyWindowListener);
		// setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	// 滑鼠判斷
	public MouseAdapter MyMouseAdapter = new MouseAdapter() {
		// ---------------------------------------------------------------------------------------------//
		// 按下滑鼠
		public void mousePressed(MouseEvent e) {
			Show.Answer_JLabel.requestFocus();
//			Scan_Generation.setVisible(false);
//			Button_Confirm.setVisible(false);
			Text_Generation.setVisible(true);
			if (e.getSource() == Scan_Generation) {
				Scan_Generation.setVisible(true);
				Button_Confirm.setVisible(true);
				Text_Generation.setVisible(false);
			}
			if (e.getSource() == Text_Generation) {
				Text_Generation.setVisible(false);
				Scan_Generation.setVisible(true);
				Button_Confirm.setVisible(true);
				Scan_Generation.setText("");
			}

			if (e.getSource() == Button_Confirm) {
				Text_Generation.setVisible(true);
				Scan_Generation.setVisible(false);
				Button_Confirm.setVisible(false);

				Show.int_Generation_Change = 1;
				try {
					// Show.Now_Generation=Integer.parseInt(GoTo_Generation.getText());
					Show.Now_Generation = Integer.parseInt(Scan_Generation.getText());
					if (Show.Now_Generation < 1)
						Show.Now_Generation = 1;
					if (Show.Now_Generation > Show.Total_Generation)
						Show.Now_Generation = Show.Total_Generation;
				} catch (Exception event) {
					Show.int_Generation_Change = 0;
				}
			}
			if (e.getSource() == Button_Stop) {
				Reset_Panel();
			}
			if (e.getSource() == Button_Start)
				if (Show.Pause == 0) {
					Show.Pause = 1;
					Button_Start.setIcon(Show.T[2]);
					Show.Button_Pause.setIcon(Show.T[2]);

				} else {
					// 播放
					Button_Start.setIcon(Show.T[3]);
					Show.Button_Pause.setIcon(Show.T[3]);
					Show.Pause = 0;
					if (Show.Now_Generation == Show.Total_Generation) {
						Show.int_Generation_Change = 1;
						Show.Now_Generation = 1;
						Show.Speed = 16;
						int_Speed = (int) Math.sqrt(Show.Speed);
						Text_Speed.setText("" + (int_Speed / 4));
					}
				}
			if (e.getSource() == Button_Recovery) {

				// 計算視窗中畫布範圍與位置
				Show.Border_X_S = (Show.Border_X_M - Show.Border_X_N) / 2;
				Show.Border_Y_S = (Show.Border_Y_M - Show.Border_Y_N) / 2;

				if (Show.Border_X_S >= Show.Border_Y_S)
					Show.Border_L = Show.Border_X_S;
				else
					Show.Border_L = Show.Border_Y_S;

				if (Show.Dimension_Mode == 1) {
					Show.Border_L = Show.Border_X_S;
					Show.Border_Y_L = Show.Border_Y_S;
				}
				Show.Border_X = Show.Border_X_N + Show.Border_X_S;
				Show.Border_Y = Show.Border_Y_N + Show.Border_Y_S;

				// 重製解空間顏色
				Show.Answer_Change = 1;
				// Show.Progress_Change=1;

			}
			// 讀檔鍵
			if (e.getSource() == Button_ReadFile) {
				Show.Pause = 1;
				Button_Start.setIcon(Show.T[2]);
				Show.Button_Pause.setIcon(Show.T[2]);
				Slider_Generation.setValue(0);
				// 開啟舊檔
				JFileChooser chooser = new JFileChooser();
				chooser.setCurrentDirectory(new java.io.File("."));
				chooser.setFileFilter(new FileNameExtensionFilter("EPin", "epin"));

				if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
					SRF = chooser.getSelectedFile().getName();
					SRF_Name = SRF.substring(0, SRF.lastIndexOf("."));
					Text_ReadFile.setText("" + SRF_Name);
					Reader_File("" + chooser.getSelectedFile());
					Reset_Panel();

				}

			}
			if (e.getSource() == Button_Accelerate) {
				Show.Speed *= 4;
				if (Show.Speed > 4096)
					Show.Speed = 16;
				int_Speed = (int) Math.sqrt(Show.Speed);
				Text_Speed.setText("" + (int_Speed / 4));
				Show.int_Speed = (int) Math.sqrt(Show.Speed);
				Show.Text_Speed.setText("" + Show.int_Speed);
			}
			if (e.getSource() == Button_Decelerate) {
				Show.Speed /= 4;
				if (Show.Speed < 16)
					Show.Speed = 4096;
				int_Speed = (int) Math.sqrt(Show.Speed);
				Text_Speed.setText("" + (int_Speed / 4));
				Show.int_Speed = (int) Math.sqrt(Show.Speed);
				Show.Text_Speed.setText("" + Show.int_Speed);
			}
			// 橡皮擦選項按鈕
			if (e.getSource() == Eraser_ToggleButton) {
				Show.Answer_Change = 1;
				// Communication_pick=(Communication_pick+1)%3;
				if (Eraser_pick == 0) {
					Eraser_pick = 1;
					Eraser_ToggleButton.setIcon(Show.T[25]);
					Show.Zoom_pick = 1;

				} else if (Eraser_pick == 1) {
					Eraser_pick = 0;
					Eraser_ToggleButton.setIcon(Show.T[24]);
					Show.Zoom_pick = 0;
				}
			}
			// 世代軸
			if (e.getSource() == Slider_Generation) {

				Show.int_Generation_Change = 1;
				Show.Now_Generation = (int) (e.getX() * (Slider_Generation.getMaximum() - 1)
						/ Slider_Generation.getWidth()) + 1;
			}
		}// 按下
			// 放開滑鼠

		public void mouseReleased(MouseEvent e) {
			// 世代軸
			if (e.getSource() == Slider_Generation) {

				Show.int_Generation_Change = 1;
				Show.Now_Generation = (int) (e.getX() * (Slider_Generation.getMaximum() - 1)
						/ Slider_Generation.getWidth()) + 1;
				if (Show.Now_Generation < 1)
					Show.Now_Generation = 1;
				if (Show.Now_Generation > Show.Total_Generation)
					Show.Now_Generation = Show.Total_Generation;
			}
		}

		// 點擊滑鼠
		public void mouseClicked(MouseEvent e) {
			// 世代軸點擊
			if (e.getSource() == Slider_Generation) {
				Show.int_Generation_Change = 1;
				Show.Now_Generation = (int) (e.getX() * (Slider_Generation.getMaximum() - 1)
						/ Slider_Generation.getWidth()) + 1;
				if (Show.Now_Generation < 1)
					Show.Now_Generation = 1;
				if (Show.Now_Generation > Show.Total_Generation)
					Show.Now_Generation = Show.Total_Generation;
			}
		}

		// 拖曳滑鼠
		public void mouseDragged(MouseEvent e) {
			// 世代軸拖移
			if (e.getSource() == Slider_Generation) {
				Show.int_Generation_Change = 1;
				Show.Now_Generation = (int) (e.getX() * (Slider_Generation.getMaximum() - 1)
						/ Slider_Generation.getWidth()) + 1;
				if (Show.Now_Generation < 1)
					Show.Now_Generation = 1;
				if (Show.Now_Generation > Show.Total_Generation)
					Show.Now_Generation = Show.Total_Generation;
			}
		}
	};// 滑鼠判斷END
		// 滑動軸判斷

	public void stateChanged(ChangeEvent e) {
		if (Show.System_Mode == 1) {
			if ((JSlider) e.getSource() == Slider_Generation) {
				Text_Fitness.setText("" + Show.Generation[Show.Now_Generation].GBest);
			}
		}
	}

	// 重置畫面(XY軸,世代數1)
	void Reset_Panel() {

		Show.Fitness_String[Show.Fitness_Amount - 1] = SRF_Name + " : ";
		// 世代數設為1
		Show.Now_Generation = 1;
		Slider_Generation.setValue(1);
		Show.Round = 0;

		Show.Pause = 1;
		Button_Start.setIcon(Show.T[2]);

		// 重製解空間顏色
		Show.Answer_Change = 1;
		Show.Progress_Change = 1;

		if (Show.System_Mode == 1) {
			// 計算視窗中畫布範圍與位置
			Show.Border_X_S = (Show.Border_X_M - Show.Border_X_N) / 2;
			Show.Border_Y_S = (Show.Border_Y_M - Show.Border_Y_N) / 2;

			if (Show.Border_X_S >= Show.Border_Y_S)
				Show.Border_L = Show.Border_X_S;
			else
				Show.Border_L = Show.Border_Y_S;

			if (Show.Dimension_Mode == 1) {
				Show.Border_L = Show.Border_X_S;
				Show.Border_Y_L = Show.Border_Y_S;
			}
			Show.Border_X = Show.Border_X_N + Show.Border_X_S;
			Show.Border_Y = Show.Border_Y_N + Show.Border_Y_S;

			Text_Fitness.setText("" + Show.Generation[Show.Now_Generation].GBest);
		} else if (Show.System_Mode == 2) {
			Text_Fitness.setText("" + Show.Map[Show.Now_Generation].Count);
		}
	}

	// 讀檔
	void Reader_File(String SRF) {
		// 0 1 2 3 4
		String Should_S_Str[] = { "Range", "Formula", "Particle", "Dimension", "Map" };
		int Should_S_int[] = { 0, 0, 0, 0, 0 };

		int temp_Total_Generation = Show.Total_Generation;
		int temp_Count = 0;
		int beanchmark = 0;

//		if(!Show.Overlapping_ToggleButton2.isSelected() )
		Show.Fitness_Amount = 0;

		Show.Fitness_Amount++;

		// 讀取解空間檔
		try {
			FileReader FR = new FileReader(SRF);
			BufferedReader BFR = new BufferedReader(FR);

			for (String temp_Str = BFR.readLine(); BFR.ready(); temp_Str = BFR.readLine()) {
				// 讀取地圖
				if (temp_Str.length() >= Should_S_Str[4].length()
						&& temp_Str.substring(0, Should_S_Str[4].length()).compareToIgnoreCase(Should_S_Str[4]) == 0) {
					if (Show.System_Mode != 2)
						Show.Mode_Change = 1;
					Show.System_Mode = 2;
					Should_S_int[4] = 1;
					Show.Total_Generation = 0;
					int i = 1;// 目前代數
					int temp_r;
					int temp_c;
					double lim;
					double temp;

					temp_Str = BFR.readLine();
					String[] tokens1 = temp_Str.split("x");
					temp_r = Integer.parseInt(tokens1[0]);
					temp_c = Integer.parseInt(tokens1[1]);
					temp_Str = BFR.readLine();
					System.out.println("Str = " + temp_Str);
					System.out.println("temp_c = " + temp_c);
					System.out.println("temp_r = " + temp_r);
					Show.Map[i] = new Map_Initial();
					Show.Map[i].row = temp_r;
					Show.Map[i].col = temp_c;
					Show.Map[i].limit = new double[temp_c][temp_r];
					for (int c_c = 0; c_c < temp_c; c_c++) {
						temp_Str = BFR.readLine();
//						System.out.println("Str = " + temp_Str);
//						System.out.println("c_c = " + c_c);
						String[] tokens5 = temp_Str.split(" ");
						for (int c_r = 0; c_r < temp_r; c_r++) {
//							System.out.println("c_r = " + c_r);
							Show.Map[i].limit[c_c][c_r] = Double.parseDouble(tokens5[c_r]);
						}
					}
//					lim = Double.parseDouble(temp_Str);
//					System.out.println("lim="+lim);
					temp_Str = BFR.readLine();
					for (temp_Str = BFR.readLine(); temp_Str != null; temp_Str = BFR.readLine()) {
						System.out.println("----");
						if(i != 1)
						{
							Show.Map[i] = new Map_Initial();
							Show.Map[i].row = Show.Map[1].row;
							Show.Map[i].col = Show.Map[1].col;
							Show.Map[i].limit = new double[Show.Map[i].col][Show.Map[i].row];
							for (int c_c = 0; c_c < temp_c; c_c++) 
								for (int c_r = 0; c_r < temp_r; c_r++) 
//									System.out.println("c_r = " + c_r);
									Show.Map[i].limit[c_c][c_r] = Show.Map[1].limit[c_c][c_r];
						}
						System.out.println("Str = " + temp_Str);
						String[] tokens2 = temp_Str.split(" ");
						Show.Map[i].Count = Integer.parseInt(tokens2[1]);
						System.out.println("Count = " + Show.Map[i].Count);
						Show.Map[i].Sensor = new int[temp_c][temp_r];
						System.out.println("temp_c = " + temp_c);
						System.out.println("temp_r = " + temp_r);
						Show.Map[i].Probability = new double[temp_c][temp_r];
						for (int j = 0; j < temp_c; j++) {
							temp_Str = BFR.readLine();
							String[] tokens3 = temp_Str.split(" ");
							for (int k = 0; k < temp_r; k++)
								Show.Map[i].Sensor[j][k] = Integer.parseInt(tokens3[k]);
						}
						// 計算機率
						for (int j = 0; j < temp_c; j++) {
							for (int k = 0; k < temp_r; k++) {
								temp = 1;
//								System.out.println("-------------");
								if (Show.Map[i].Sensor[j][k] == 1)
									Show.Map[i].Probability[j][k] = -1;
								else {
									System.out.println("j="+j);
									System.out.println("k="+k);
									for (int ij = j - 6; ij < j + 6; ij++) {
										for (int ik = k - 6; ik < k + 6; ik++) {
//											System.out.println("----");
//											System.out.println("ij="+ij);
//											System.out.println("ik="+ik);
											if (ij >= 0 && ik >= 0 && ij < temp_c && ik < temp_r) {
												if (Show.Map[i].Sensor[ij][ik] == 1) {
													double distance = Math
															.sqrt((ij - j) * (ij - j) + (ik - k) * (ik - k));
													// if(distance<=5)
													temp *= (1 - (double) 1 / distance);
//													System.out.println("D="+distance);
//													System.out.println("p="+temp);
												}
											}
										}
									}
									Show.Map[i].Probability[j][k] = 1 - temp;
								}
								System.out.println("Pro="+Show.Map[i].Probability[j][k]);
							}
						}
						
						i++;
						System.out.println("i="+i);

						
					}
					System.out.println("----1");
					Show.Total_Generation = i - 1;
					System.out.println("G = "+Show.Total_Generation);
					Show.temp_Map = new Map_Initial();
					Show.temp_Map.row = Show.Map[Show.Total_Generation].row;
					Show.temp_Map.col = Show.Map[Show.Total_Generation].col;
					Show.temp_Map.limit = Show.Map[Show.Total_Generation].limit;
					Show.temp_Map.Count = Show.Map[Show.Total_Generation].Count;
					Show.temp_Map.Sensor = new int[Show.temp_Map.col][Show.temp_Map.row];
					Show.temp_Map.Probability = new double[Show.temp_Map.col][Show.temp_Map.row];
					for (int j = 0; j < Show.temp_Map.col; j++) {
						for (int k = 0; k < Show.temp_Map.row; k++) {
							Show.temp_Map.Sensor[j][k] = Show.Map[Show.Total_Generation].Sensor[j][k];
							Show.temp_Map.Probability[j][k] = Show.Map[Show.Total_Generation].Probability[j][k];
						}
					}

				}

				// 讀取粒子紀錄檔Particle
				else if (temp_Str.length() >= Should_S_Str[2].length()
						&& temp_Str.substring(0, Should_S_Str[2].length()).compareToIgnoreCase(Should_S_Str[2]) == 0) {
					if (Show.System_Mode != 1)
						Show.Mode_Change = 1;
					Show.System_Mode = 1;
					Should_S_int[2] = 1;

					Show.Total_Generation = 0;

					int Interval_temp = 1;// 目前當代的粒子數
					int Bit_temp = 1;// 目前粒子的bit數
					int i = 1;// 目前代數

					for (temp_Str = BFR.readLine(); temp_Str != null; temp_Str = BFR.readLine()) {
						temp_Count++;
						if (temp_Count > temp_Total_Generation)
							temp_Count = temp_Total_Generation;

						int j = 0;// 目前bit數

						// 根據切割前後字串
						String[] tokens1 = temp_Str.split(":");
						// 切割出每個粒子資訊((bit+probability+bitinfo)*n)
						String[] tokens2 = tokens1[1].trim().split("/");
						// 代數+當代最佳解
						String[] tokens4 = tokens1[0].trim().split(" ");
						// 粒子數
						Interval_temp = tokens2.length;
//						System.out.println("tokens4[1]="+tokens4[1]);
//						System.out.println("tokens2[0]="+tokens2[0]);
//						System.out.println("Int_tmp="+Interval_temp);

						Show.Generation[i] = new Generation_Initial();
						Show.Generation[i].Amount = Show.Fitness_Amount;
						Show.Generation[i].Particle = Interval_temp;
						Show.Generation[i].GBest = Double.parseDouble(tokens4[1]);
						Show.Generation[i].CBest = 0;
						Show.Generation[i].X = new Particle_Initial[Interval_temp];
						
						if (i > 1) {
							Show.Generation[i].Evaluation = Show.Generation[i-1].Evaluation+Interval_temp;
						}
						else
							Show.Generation[1].Evaluation = Interval_temp;

/*						// 放入目前最佳解的代數位置
						if (i > 1) {
							if (Show.Generation[i - 1].GBest != Show.Generation[i].GBest) {
								Show.Generation[i].GBest_gen = i;
								System.out.println("i=" + i);
							} else
								Show.Generation[i].GBest_gen = Show.Generation[i - 1].GBest_gen;
						} else
							Show.Generation[i].GBest_gen = 1;
						System.out.println("i="+i);
*/
						// 放入各粒子
						for (int k = 0; k < Interval_temp; k++) {
							Show.Generation[i].X[k] = new Particle_Initial();

							String[] tokens3 = tokens2[k].trim().split(" ");
							Bit_temp = tokens3.length - 2;
							Show.Generation[i].X[k].Amount = Bit_temp;
							Show.Generation[i].X[k].Fitness = Double.parseDouble(tokens3[0]);
							Show.Generation[i].X[k].PInfo = tokens3[1];
							Show.Generation[i].X[k].Bit = new int[Bit_temp];
							Show.Generation[i].X[k].BitInfo = new String[Bit_temp];
							Show.Generation[i].X[k].Probability = new float[Bit_temp];
							//尋找當代最佳解
							
							if (Show.Generation[i].X[k].Fitness > Show.Generation[i].X[Show.Generation[i].CBest].Fitness)
								Show.Generation[i].CBest = k;
							

							// 放入各Bit
							for (int m = 0; m < (Bit_temp); m++) {
								String[] tokens5 = tokens3[m + 2].trim().split(",");
								Show.Generation[i].X[k].Bit[m] = Integer.parseInt(tokens5[0]);
								Show.Generation[i].X[k].Probability[m] = Float.parseFloat(tokens5[1]);
								if(tokens5.length>3)
									Show.Generation[i].X[k].BitInfo[m] = tokens5[2];
//								System.out.println("BitInfo="+Show.Generation[i].X[k].BitInfo[m]);
							}
						}
						System.out.println("i=" + i);
						if(i>1) {
							int gb_g = Show.Generation[i-1].GBest_gen;
//							System.out.println("CBest_F="+Show.Generation[i].X[Show.Generation[i].CBest].Fitness);
//							System.out.println("GBest_F="+Show.Generation[gb_g].X[Show.Generation[gb_g].CBest].Fitness);
							if(Show.Generation[i].X[Show.Generation[i].CBest].Fitness > Show.Generation[gb_g].X[Show.Generation[gb_g].CBest].Fitness) {
								Show.Generation[i].GBest_gen = i;
							} else
								Show.Generation[i].GBest_gen = Show.Generation[i - 1].GBest_gen;
								
						} else
							Show.Generation[i].GBest_gen = 1;
						
						
//						System.out.println("GBest="+Show.Generation[i].GBest_gen);
//						System.out.println("CBest="+Show.Generation[i].CBest);
						i++;
					}
					Show.Total_Generation = i - 1;
//					System.out.println("Tol_Gen="+Show.Total_Generation);
//					System.out.println("Fitness_Amount="+Show.Fitness_Amount);
//					System.out.println(Show.Generation[Show.Total_Generation].X[0]);
//					if(Show.Total_Generation>718)
//						System.out.println("X[2]="+Show.Generation[1].X[2]);
//					System.out.println("----");
				}

			}
			BFR.close();
			System.out.println("------");

			// 世代數相關
			Show.Now_Generation = 1;

			Slider_Generation.setMaximum(Show.Total_Generation);

			System.arraycopy(Show.Generation, 0, Show.temp_Generation, 0, Show.Generation.length);

		} catch (Exception fe) {
			JOptionPane.showMessageDialog(null, "錯誤碼：" + fe.toString() + "\n請檢查檔案。", "錯誤", JOptionPane.ERROR_MESSAGE);
		}

		for (int i = 0; i < 3; i++)
			if (Should_S_int[i] == 0) {
				Input_Formula.setText("找不到：" + Should_S_Str[i] + "\n請重開並檢查檔案。");
			}

		Show.Input_File.setText("File : " + SRF_Name);
//		System.out.println("SRF_Name="+SRF_Name);
//		Show.Drawer_Panel();		
	}

	// 捕捉關閉視窗
	public WindowAdapter MyWindowListener = new WindowAdapter() {

		// System.out.println("焦點");
		public void windowActivated(WindowEvent e) {

			if (Player_file != null) {
				Reader_File("" + Player_file.getAbsolutePath());

				String SRF = Player_file.getName();
				SRF_Name = SRF.substring(0, SRF.lastIndexOf("."));
				Text_ReadFile.setText("" + SRF_Name);

				Reset_Panel();

				System.out.println("SRF_Name=" + SRF_Name);

				Player_file = null;
			}
		}

		// System.out.println("失焦");
		public void windowDeactivated(WindowEvent e) {
		}

		public void windowClosing(WindowEvent e) {
			dispose();
		}

		public void windowClosed(WindowEvent e) {
			Player_file = null;
			SRF = null;

			if (Show.JFrame_mod == 1)
				Show.JFrame_mod = 0;
			Show.Progress_Change = 1;

			Slider_Generation.setValue(1);
			Slider_Generation.setMaximum(1);
			Slider_Generation.setMaximum(1);

			Text_ReadFile.setText("");
			Text_Generation.setText("1/1");
			Show.Total_Generation = Show.Now_Generation = 1;
			Text_Fitness.setText("0.0");
			Input_Formula.setText("0");

			Show.Button_Player.setSelected(false);
			Show.Pause = 1;
			Button_Start.setIcon(Show.T[2]);

			Show.Postfix[0] = 0;
			Show.Answer_Change = 1;

		}
	};
}

class Converse extends EPanel {
	
	JPanel Chart_JPanel = new JPanel();
	JLabel Chart_JLabel;
	static BufferedImage Chart_Image;
	JButton aa;
	
	Converse(){
		
		Chart_JPanel.setSize(400, 400);
		Chart_JPanel.setLayout(null);
		Chart_Image = new BufferedImage(400, 400, BufferedImage.TYPE_INT_RGB);
		Draw_chart();
		Chart_JLabel = new JLabel(new ImageIcon(Chart_Image));
		Chart_JLabel.setBounds(0, 0, 400, 400);
		Chart_JLabel.setLayout(null);
		
		
		
		Chart_JPanel.add(Chart_JLabel);
//		Chart_JPanel.setBackground(Color.RED);
		add(Chart_JPanel);
		System.out.println("aa");
		
		setLayout(null);
		setBounds(855, 160, 400, 400);
//		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
//		Draw_chart();
		
	}
	static void Draw_chart() {
		Graphics g = Chart_Image.getGraphics();
		Graphics2D g2D = (Graphics2D) g;
		int x_min = 0;
		int x_max = 0;
		double y_min = 0;
		double y_max = 0;
		double x_dif;
		double y_dif;
		int now_eva = 1;
		double x1=0, y1=0, x2=0, y2=0, x_tmp, y_tmp;
		int x_start = 60;
		int y_start = 20;
		
//		System.out.println("bb");
		g.setColor(new Color(255,255,255));
		g.fillRect(0, 0, 400, 400);
		g.setColor(new Color(0, 0, 0));
		g.drawLine(x_start, y_start, x_start, y_start+300);
		g.drawLine(x_start, y_start+300, x_start+300, y_start+300);
		for(int j=1;j<10;j++) {
			g.drawLine(x_start, (320-20)/10*j+20, x_start+2, (320-20)/10*j+20);
			g.drawLine((300)/10*j+x_start, 315, (300)/10*j+x_start, 320);
		}
		
		g.drawString("fitness", 5, 14);
		g.drawString("evaluation", 170, 350);
		if(Show.Fitness_Amount++ > 0)
		{
			x_max = Show.Generation[Show.Total_Generation].Evaluation;
			y_max=Show.Generation[Show.Total_Generation].GBest;
			y_min=Show.Generation[1].GBest;
			x_dif = (double)x_max-x_min;
			y_dif = (double)y_max-y_min;
			g.drawString(""+x_max, 340, 340);
			g.drawString(""+x_min, x_start, 340);
			g.drawString(""+y_max, 5, 30);
			g.drawString(""+y_min, 5, 320);
			for(int i=1;i<=Show.Total_Generation;i++) {
				
				x1 = (double)now_eva/x_dif*300+x_start;
				y1 = y_start-(300*(Show.Generation[i].GBest-y_max)/y_dif);
				if(i>1) {
					x_tmp = x2;
					y_tmp = y2;
					g.drawLine((int)x1, (int)y1, (int)x_tmp, (int)y_tmp);
				}
				x2 = (double)Show.Generation[i].Evaluation/x_dif*300+x_start;
				y2 = y1;
				g.drawLine((int)x1, (int)y1, (int)x2, (int)y2);
				System.out.println("i="+i);
				System.out.println("now_eva="+now_eva);
				System.out.println("now_eva="+Show.Generation[i].Particle);
				System.out.println("x1="+x1);
				System.out.println("y1="+y1);
				System.out.println("x2="+x2);
				System.out.println("y2="+y2);
				now_eva += Show.Generation[i].Particle;
			}
			
		}
	}
}

class Threading extends Thread {
	public void run() {
		// System.out.println("Thread runnung...");
		long temp_time = 0;
		int temp = 0;
		int temp_s = 0;
		while (Show.Video_mode == 1) {
			if (System.currentTimeMillis() - temp_time > 50) {
				temp_time = System.currentTimeMillis();
				try {
					Screenshots("EPanel_temp", Show.Video_x1, Show.Video_y1, Show.Video_x2, Show.Video_y2);
					Show.Video_out_file.writeFrame(ImageIO.read(new File("EPanel_temp")));
				} catch (Exception e) {
					e.printStackTrace();
				}

				if (temp_s++ >= 20) {
					temp_s = 0;
					if (temp == 0)
						Show.Button_Video.setIcon(Show.T[32]);
					else
						Show.Button_Video.setIcon(Show.T[31]);
					temp = 1 - temp;
				}
			}
		}

		try {
			Show.Video_out_file.close();
			new File("EPanel_temp").delete();
		} catch (Exception es) {
			es.printStackTrace();
		}
		Show.Button_Video.setIcon(Show.T[30]);
	}

	// 截圖
	void Screenshots(String path, int x1, int y1, int x2, int y2) throws Exception {

		Robot robot = new Robot();
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		Rectangle rect = new Rectangle(x1, y1, x2, y2);
		BufferedImage image = robot.createScreenCapture(rect);
		ImageIO.write(image, "jpg", new File(path));

	}

}

//共用函式
class EPanel extends JFrame {
	EPanel() {
	}

	// 讀取方程式 轉換為後置
	static void Expand_Equation(String Equation_S, double Postfix[]) {
		for (int i = 0; i < 200; i++) {

		}
	}

	static void Screen(int x1, int y1, int x2, int y2) {
		SimpleDateFormat sdFormat = new SimpleDateFormat("yyyyMMdd_HHmmss_SSS");
		Date current = new Date();
		// System.out.println(sdFormat.format(current));
		try {
			Screenshots("EPanelcam_" + sdFormat.format(current) + ".jpg", x1, y1, x2, y2);
		} catch (Exception es) {
			es.printStackTrace();
		}

	}

	// 截圖
	static void Screenshots(String path, int x1, int y1, int x2, int y2) throws Exception {

		Robot robot = new Robot();
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		Rectangle rect = new Rectangle(x1, y1, x2, y2);
		BufferedImage image = robot.createScreenCapture(rect);
		ImageIO.write(image, "jpg", new File(path));

	}

	// 存檔時詢問覆蓋問題
	class MyChooser extends JFileChooser {
		MyChooser(String path) {
			super(path);
		}

		/*
		 * 同樣是覆蓋approveSelection方法，首先獲得輸入檔的路徑，然後判斷其是否在目前的目錄下，如果存在，那麼彈出對話方塊詢問是否需要覆蓋當前檔，
		 * 如果選擇取消，則回到保存對話方塊，用戶可以繼續進行保存的操作。
		 */
		public void approveSelection() {
			File file = this.getSelectedFile();
			if (file.exists()) {
				int copy = JOptionPane.showConfirmDialog(null, "檔案" + file.getName() + "已經存在。\n您要取代它嗎?", "EPanel",
						JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
				if (copy == JOptionPane.YES_OPTION)
					super.approveSelection();
			} else
				super.approveSelection();
		}
	}
}
