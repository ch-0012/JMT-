package GUI;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import library.BasketMgr;
import library.BestMgr;
import library.Book;
import library.BookLoan;
import library.BookMgr;
import library.Library;
import library.LoanMgr;
import library.LoanedMgr;

public class LibraryProgram {
	static Library lib = Library.getInstance();

	public static void main(String args[]) {
		lib.run();
		startGUI();
	}

	public static void startGUI() {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}

	static JFrame mainFrame = new JFrame("비대면 도서 대출");

	private static void createAndShowGUI() {
		// Create and set up the window.
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel test = new JPanel();
		test.setLayout(new BorderLayout());

		setupLoginPane();
		test.add(loginPage, BorderLayout.CENTER);
		mainFrame.getContentPane().add(test);
		// Display the window.

		mainFrame.setResizable(false);
		mainFrame.setSize(800, 600);
		mainFrame.setLocationRelativeTo(null);
		mainFrame.setVisible(true);
	}

	private static JPanel setPage;
	private static JPanel mainPage;
	private static JPanel loginPage;
	private static JPanel recPage;
	private static JPanel loanedPane;
	private static JPanel basketPane;
	private static JPanel loanPage;
	private static JPanel leftPane;
	private static JPanel rightPane;
	private static JPanel bestPane;
	static CardLayout card = new CardLayout();

	private static void setupPage() {
		setPage = new JPanel();
		setPage.setLayout(new BorderLayout(3, 0));
		leftPane = new JPanel();
		leftPane.setLayout(new FlowLayout());
		infoLabel welcome = new infoLabel("환영합니다 " + lib.login_user.name + "님");
		welcome.setFont(new Font("나눔고딕 ExtraBold", Font.BOLD, 15));
		welcome.setPreferredSize(new Dimension(150, 40));
		infoLabel nowLoan = new infoLabel("현재 대출: " + lib.login_user.loanNum() + "권");
		infoLabel canLoan = new infoLabel("가능 대출: " + lib.login_user.canLoanNum() + "권");
		infoLabel overLoan = new infoLabel("연체 도서: " + lib.login_user.overLoanNum() + "권");
		menuBtn logout = new menuBtn("로그아웃");
		JLabel blank = new JLabel("");
		blank.setPreferredSize(new Dimension(200, 40));
		infoLabel menuLabel = new infoLabel("메뉴");
		menuLabel.setFont(new Font("나눔고딕 ExtraBold", Font.BOLD, 15));
		menuBtn allBook = new menuBtn("전체 도서");
		menuBtn recBook = new menuBtn("추천 도서");
		menuBtn bestBook = new menuBtn("인기 도서");
		infoLabel myPageLabel = new infoLabel("마이페이지");
		myPageLabel.setFont(new Font("나눔고딕 ExtraBold", Font.BOLD, 15));
		menuBtn basketBtn = new menuBtn("장바구니");
		menuBtn loanBtn = new menuBtn("대출 현황");

		leftPane.add(welcome);
		leftPane.add(nowLoan);
		leftPane.add(canLoan);
		leftPane.add(overLoan);
		leftPane.add(logout);
		leftPane.add(blank);
		leftPane.add(menuLabel);
		leftPane.add(allBook);
		leftPane.add(recBook);
		leftPane.add(bestBook);
		leftPane.add(myPageLabel);
		leftPane.add(basketBtn);
		leftPane.add(loanBtn);
		leftPane.setPreferredSize(new Dimension(150, 600));

		rightPane = new JPanel();
		rightPane.setLayout(card);
		setupMainPane();
		rightPane.add(mainPage, "main");
		setupBasketPane();
		rightPane.add(basketPane, "basket");
		setupLoanedPane();
		rightPane.add(loanedPane, "loaned");
		setupRecPane();
		rightPane.add(recPage, "rec");
		setupBestPane();
		rightPane.add(bestPane, "best");
		card.show(rightPane, "main");

		setPage.setBackground(Color.LIGHT_GRAY);
		setPage.add(leftPane, BorderLayout.WEST);
		setPage.add(rightPane, BorderLayout.CENTER);
		logout.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				lib.logout();
				setupLoginPane();
				frameRefresh(loginPage);
			}
		});

		allBook.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setupMainPane();
				card.show(rightPane, "main");
			}
		});

		basketBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setupBasketPane();
				rightPane.add(basketPane, "basket");
				card.show(rightPane, "basket");
			}
		});

		loanBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setupLoanedPane();
				card.show(rightPane, "loaned");
			}
		});
		recBook.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setupRecPane();
				card.show(rightPane, "rec");
			}
		});
		bestBook.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setupBestPane();
				card.show(rightPane, "best");
			}
		});

	}

	private static JPanel imgPage;
	static BufferedImage img = null;
	static BufferedImage img2 = null;
	static BufferedImage backgroundImg = null;
	static BufferedImage backgroundImg2 = null;

	private static void setupLoginPane() {
		loginPage = new JPanel();

		try {
			img = ImageIO.read(new File("C:/Users/user/eclipse-workspace/JMT 팀 프로젝트/src/img/back.jpg"));
			backgroundImg = ImageIO.read(new File("C:/Users/user/eclipse-workspace/JMT 팀 프로젝트/src/img/back2.jpg"));
			backgroundImg2 = ImageIO.read(new File("C:/Users/user/eclipse-workspace/JMT 팀 프로젝트/src/img/back3.jpg"));
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "이미지 불러오기 실패");
		}
		imgPage = new JPanel() {
			public void paint(Graphics g) {
				g.drawImage(img, 0, 0, null);
				setOpaque(false);
				super.paintComponent(g);
			}
		};
		imgPage.setLayout(null);
		imgPage.setBounds(0, 0, 800, 200);

		JButton userLoginBtn = new JButton("로그인");
		JButton adminLoginBtn = new JButton("관리자");
		JTextField idField = new JTextField("아이디");
		JTextField pwField = new JTextField("비밀번호");
		JLabel loginFail = new JLabel("로그인 실패");
		loginFail.setForeground(Color.RED);

		loginPage.setLayout(null);
		TitleLabel title = new TitleLabel("비대면 도서 대출");
		title.setForeground(Color.WHITE);
		loginPage.add(title);

		idField.setBounds(200, 250, 400, 60);
		pwField.setBounds(200, 330, 400, 60);
		loginFail.setBounds(200, 200, 800, 40);
		loginFail.setVisible(false);
		loginPage.add(idField);
		loginPage.add(pwField);
		loginPage.add(loginFail);

		userLoginBtn.setBounds(200, 410, 190, 60);
		adminLoginBtn.setBounds(410, 410, 190, 60);
		userLoginBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (lib.userLogin(idField.getText(), pwField.getText())) {
					setupPage();
					frameRefresh(setPage);
				} else {
					loginFail.setVisible(true);
				}
			}
		});
		idField.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 1 && idField.getText().contentEquals("아이디")) {
					idField.setText("");
				}
			}

			public void mouseExited(MouseEvent e) {
				if (idField.getText().contentEquals(""))
					idField.setText("아이디");
			}
		});
		pwField.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 1 && pwField.getText().contentEquals("비밀번호")) {
					pwField.setText("");
				}
			}

			public void mouseExited(MouseEvent e) {
				if (pwField.getText().contentEquals(""))
					pwField.setText("비밀번호");
			}
		});
		adminLoginBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (lib.adminLogin(idField.getText(), pwField.getText())) {
					mainFrame.setVisible(false);
					lib.adminMenu();
				} else {
					loginFail.setVisible(true);
				}
			}
		});
		loginPage.add(imgPage);
		loginPage.add(userLoginBtn);
		loginPage.add(adminLoginBtn);
	}

	private static void setupMainPane() {
		mainPage = new JPanel();
		mainPage.setLayout(null);

		try {
			img = ImageIO.read(new File("C:/Users/user/eclipse-workspace/JMT 팀 프로젝트/src/img/search.jpg"));
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "이미지 불러오기 실패");
		}
		imgPage = new JPanel() {
			public void paint(Graphics g) {
				g.drawImage(backgroundImg, 0, 0, null);
				setOpaque(false);
				super.paintComponent(g);
			}
		};
		imgPage.setLayout(null);
		imgPage.setBounds(0, 0, 800, 190);

		TableSelectionDemo bookTable = new TableSelectionDemo();
		bookTable.addComponentsToPane(BookMgr.getInstance());
		bookTable.setBounds(40, 250, 500, 250);
		bookTable.size("제목", 200);
		JPanel topPane = new JPanel();
		topPane.setBounds(0, 190, 590, 50);
		TitleLabel title = new TitleLabel("메인화면");

		JTextField searchField = new JTextField("", 40);
		topPane.add(searchField);
		ImageIcon search = new ImageIcon(img);
		JButton searchBtn = new JButton(search);
		searchBtn.setBackground(Color.white);
		topPane.add(searchBtn);

		searchBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				bookTable.loadData(searchField.getText());
			}
		});

		bookTable.table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					bookTable.showInfo();
				}
			}
		});

		mainPage.add(title);
		mainPage.add(topPane);
		mainPage.add(bookTable);
		mainPage.add(imgPage);
	}
	
	private static void setupRecPane() {
		recPage = new JPanel();
		recPage.setLayout(null);
		recPage.add(new TitleLabel("추천 도서"));
		
		imgPage = new JPanel() {
			public void paint(Graphics g) {
				g.drawImage(backgroundImg, 0, 0, null);
				setOpaque(false);
				super.paintComponent(g);
			}
		};
		imgPage.setLayout(null);
		imgPage.setBounds(0, 0, 800, 190);

		ArrayList<Book> recList = lib.login_user.recommend();
		JPanel book1 = new JPanel();
		book1.setLayout(new GridLayout(1, 1));
		JLabel book1Title = new JLabel(recList.get(0).title);
		JLabel book1Author = new JLabel(recList.get(0).author);
		book1Author.setHorizontalAlignment(JLabel.CENTER);
		JPanel book2 = new JPanel();
		book2.setLayout(new GridLayout(1, 1));
		JLabel book2Title = new JLabel(recList.get(1).title);
		JLabel book2Author = new JLabel(recList.get(1).author);
		book2Author.setHorizontalAlignment(JLabel.CENTER);
		JPanel book3 = new JPanel();
		book3.setLayout(new GridLayout(1, 1));
		JLabel book3Title = new JLabel(recList.get(2).title);
		JLabel book3Author = new JLabel(recList.get(2).author);
		book3Author.setHorizontalAlignment(JLabel.CENTER);
		book1.setBounds(60, 200, 150, 200);
		book1Title.setBounds(60, 410, 150, 20);
		book1Author.setBounds(60, 430, 150, 20);
		book2.setBounds(240, 200, 150, 200);
		book2Title.setBounds(240, 410, 150, 20);
		book2Author.setBounds(240, 430, 150, 20);
		book3.setBounds(420, 200, 150, 200);
		book3Title.setBounds(420, 410, 150, 20);
		book3Author.setBounds(420, 430, 150, 20);

		File imgFile = new File("C:/Users/user/eclipse-workspace/JMT 팀 프로젝트/src/img");
		String[] imgList = imgFile.list();
		String[] path = new String[3];
		for(int i=0; i<3; i++) {
			for(int j=0; j<imgList.length; j++) {
				if(imgList[j].equals(recList.get(i).isbn + ".jpg")) {
					path[i] = "C:/Users/user/eclipse-workspace/JMT 팀 프로젝트/src/img/" + recList.get(i).isbn + ".jpg";
					break;
				}
				else
					path[i] = "C:/Users/user/eclipse-workspace/JMT 팀 프로젝트/src/img/null.jpg";
			}
		}
		
		ImageIcon img1 = new ImageIcon(path[0]);
		ImageIcon img2 = new ImageIcon(path[1]);
		ImageIcon img3 = new ImageIcon(path[2]);
		JLabel image1 = new JLabel("", img1, JLabel.CENTER);
		book1.add(image1);
		JLabel image2 = new JLabel("", img2, JLabel.CENTER);
		book2.add(image2);
		JLabel image3 = new JLabel("", img3, JLabel.CENTER);
		book3.add(image3);

		recPage.add(book1Title);
		recPage.add(book1Author);
		recPage.add(book2Title);
		recPage.add(book2Author);
		recPage.add(book3Title);
		recPage.add(book3Author);
		recPage.add(book1);
		recPage.add(book2);
		recPage.add(book3);
		recPage.add(imgPage);
	}
	
	   private static void setupBestPane() {
		      bestPane = new JPanel();
		      bestPane.setLayout(null);
		      bestPane.add(new TitleLabel("인기 도서"));

		      imgPage = new JPanel() {
					public void paint(Graphics g) {
						g.drawImage(backgroundImg, 0, 0, null);
						setOpaque(false);
						super.paintComponent(g);
					}
				};
				imgPage.setLayout(null);
				imgPage.setBounds(0, 0, 800, 190);
		      
		      JTabbedPane tabPane = new JTabbedPane();
		      tabPane.setBounds(40, 250, 570, 220);

		      JPanel PaneAll = new JPanel();
		      JPanel Pane0 = new JPanel();
		      JPanel Pane1 = new JPanel();
		      JPanel Pane2 = new JPanel();
		      JPanel Pane3 = new JPanel();
		      JPanel Pane4 = new JPanel();
		      JPanel Pane5 = new JPanel();
		      JPanel Pane6 = new JPanel();
		      JPanel Pane7 = new JPanel();
		      JPanel Pane8 = new JPanel();
		      JPanel Pane9 = new JPanel();

		      TableSelectionDemo TableAll = new TableSelectionDemo();
		      TableAll.addComponentsToPane(BestMgr.getInstace(),"");
		      TableAll.size("제목", 300);
		      TableSelectionDemo Table0 = new TableSelectionDemo();
		      Table0.addComponentsToPane(BestMgr.getInstace(),0+"");
		      Table0.size("제목", 300);
		      TableSelectionDemo Table1 = new TableSelectionDemo();
		      Table1.addComponentsToPane(BestMgr.getInstace(),1+"");
		      Table1.size("제목", 300);
		      TableSelectionDemo Table2 = new TableSelectionDemo();
		      Table2.addComponentsToPane(BestMgr.getInstace(),2+"");
		      Table2.size("제목", 300);
		      TableSelectionDemo Table3 = new TableSelectionDemo();
		      Table3.addComponentsToPane(BestMgr.getInstace(),3+"");
		      Table3.size("제목", 300);
		      TableSelectionDemo Table4 = new TableSelectionDemo();
		      Table4.addComponentsToPane(BestMgr.getInstace(),4+"");
		      Table4.size("제목", 300);
		      TableSelectionDemo Table5 = new TableSelectionDemo();
		      Table5.addComponentsToPane(BestMgr.getInstace(),5+"");
		      Table5.size("제목", 300);
		      TableSelectionDemo Table6 = new TableSelectionDemo();
		      Table6.addComponentsToPane(BestMgr.getInstace(),6+"");
		      Table6.size("제목", 300);
		      TableSelectionDemo Table7 = new TableSelectionDemo();
		      Table7.addComponentsToPane(BestMgr.getInstace(),7+"");
		      Table7.size("제목", 300);
		      TableSelectionDemo Table8 = new TableSelectionDemo();
		      Table8.addComponentsToPane(BestMgr.getInstace(),8+"");
		      Table8.size("제목", 300);
		      TableSelectionDemo Table9 = new TableSelectionDemo();
		      Table9.addComponentsToPane(BestMgr.getInstace(),9+"");
		      Table9.size("제목", 300);

		      PaneAll.add(TableAll);
		      Pane0.add(Table0);
		      Pane1.add(Table1);
		      Pane2.add(Table2);
		      Pane3.add(Table3);
		      Pane4.add(Table4);
		      Pane5.add(Table5);
		      Pane6.add(Table6);
		      Pane7.add(Table7);
		      Pane8.add(Table8);
		      Pane9.add(Table9);

		      tabPane.add("전체", PaneAll);
		      tabPane.add("총류", Pane0);
		      tabPane.add("철학", Pane1);
		      tabPane.add("종교", Pane2);
		      tabPane.add("사회학", Pane3);
		      tabPane.add("언어", Pane4);
		      tabPane.add("자연과학", Pane5);
		      tabPane.add("기술과학", Pane6);
		      tabPane.add("예술", Pane7);
		      tabPane.add("문학", Pane8);
		      tabPane.add("역사", Pane9);
		      

		      TableAll.table.addMouseListener(new MouseAdapter() {
		         @Override
		         public void mouseClicked(MouseEvent e) {
		            if (e.getClickCount() == 2) {
		               TableAll.showInfo();
		            }
		         }
		      });
		      Table0.table.addMouseListener(new MouseAdapter() {
		         @Override
		         public void mouseClicked(MouseEvent e) {
		            if (e.getClickCount() == 2) {
		               Table0.showInfo();
		            }
		         }
		      });
		      Table1.table.addMouseListener(new MouseAdapter() {
		         @Override
		         public void mouseClicked(MouseEvent e) {
		            if (e.getClickCount() == 2) {
		               Table1.showInfo();
		            }
		         }
		      });
		      Table2.table.addMouseListener(new MouseAdapter() {
		         @Override
		         public void mouseClicked(MouseEvent e) {
		            if (e.getClickCount() == 2) {
		               Table2.showInfo();
		            }
		         }
		      });
		      Table3.table.addMouseListener(new MouseAdapter() {
		         @Override
		         public void mouseClicked(MouseEvent e) {
		            if (e.getClickCount() == 2) {
		               Table3.showInfo();
		            }
		         }
		      });
		      Table4.table.addMouseListener(new MouseAdapter() {
		         @Override
		         public void mouseClicked(MouseEvent e) {
		            if (e.getClickCount() == 2) {
		               Table4.showInfo();
		            }
		         }
		      });
		      Table5.table.addMouseListener(new MouseAdapter() {
		         @Override
		         public void mouseClicked(MouseEvent e) {
		            if (e.getClickCount() == 2) {
		               Table5.showInfo();
		            }
		         }
		      });
		      Table6.table.addMouseListener(new MouseAdapter() {
		         @Override
		         public void mouseClicked(MouseEvent e) {
		            if (e.getClickCount() == 2) {
		               Table6.showInfo();
		            }
		         }
		      });
		      Table7.table.addMouseListener(new MouseAdapter() {
		         @Override
		         public void mouseClicked(MouseEvent e) {
		            if (e.getClickCount() == 2) {
		               Table7.showInfo();
		            }
		         }
		      });
		      Table8.table.addMouseListener(new MouseAdapter() {
		         @Override
		         public void mouseClicked(MouseEvent e) {
		            if (e.getClickCount() == 2) {
		               Table8.showInfo();
		            }
		         }
		      });
		      Table9.table.addMouseListener(new MouseAdapter() {
		         @Override
		         public void mouseClicked(MouseEvent e) {
		            if (e.getClickCount() == 2) {
		               Table9.showInfo();
		            }
		         }
		      });
		      bestPane.add(tabPane);
		      bestPane.add(imgPage);
		   }
	
	private static void setupBasketPane() {
		basketPane = new JPanel();
		basketPane.setLayout(null);
		TitleLabel title = new TitleLabel("장바구니");
		title.setForeground(Color.white);
		basketPane.add(title);
		
		imgPage = new JPanel() {
			public void paint(Graphics g) {
				g.drawImage(backgroundImg2, 0, 0, null);
				setOpaque(false);
				super.paintComponent(g);
			}
		};
		imgPage.setLayout(null);
		imgPage.setBounds(0, 0, 800, 150);
		
		
		TableSelectionDemo basketTable = new TableSelectionDemo();
		basketTable.setBounds(50, 180, 500, 300);
		BasketMgr basket = BasketMgr.getInstance();
		basket.setBasket(lib.login_user);
		basketTable.addComponentsToPane(basket);
		basketTable.size("제목", 180);
		basketTable.setCheckBox();
		JButton loanBtn = new JButton("대출");
		loanBtn.setBounds(260, 490, 80, 50);
		JButton deleteBtn = new JButton("선택 삭제");
		deleteBtn.setBounds(460, 490, 90, 30);

		loanBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ArrayList<Book> checkedBook = new ArrayList<>();
				for (int i = 0; i < lib.basket.basketedBookList.size(); i++) {
					Book book = lib.basket.basketedBookList.get(i);
					if (Boolean.valueOf(basketTable.tableModel.getValueAt(i, 4).toString()) && book.canLoan())
						checkedBook.add(book);
					setupLoanPane(checkedBook, null);
					frameRefresh(loanPage);
				}
			}
		});
		
		deleteBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ArrayList<Book> checkedBook = new ArrayList<>();
				for (int i = 0; i < lib.basket.basketedBookList.size(); i++) {
					Book book = lib.basket.basketedBookList.get(i);
					if (Boolean.valueOf(basketTable.tableModel.getValueAt(i, 4).toString()) && book.canLoan())
						checkedBook.add(book);
				}
				lib.basket.sub(checkedBook);
				setupBasketPane();
				rightPane.add(basketPane, "basket");
				card.show(rightPane, "basket");
			}
		});

		basketPane.add(basketTable);
		basketPane.add(loanBtn);
		basketPane.add(deleteBtn);
		basketPane.add(imgPage);
	}

	private static void setupLoanedPane() {
		loanedPane = new JPanel();
		TitleLabel title = new TitleLabel("대출현황");
		title.setForeground(Color.white);
		loanedPane.add(title);
		loanedPane.setLayout(null);

		imgPage = new JPanel() {
			public void paint(Graphics g) {
				g.drawImage(backgroundImg2, 0, 0, null);
				setOpaque(false);
				super.paintComponent(g);
			}
		};
		imgPage.setLayout(null);
		imgPage.setBounds(0, 0, 800, 150);
		
		TableSelectionDemo loanTable = new TableSelectionDemo();
		loanTable.setBounds(50, 180, 500, 300);
		LoanedMgr loan = LoanedMgr.getInstance();
		loan.setLoan(lib.login_user);
		loanTable.addComponentsToPane(loan);
		loanTable.size("제목", 180);

		loanedPane.add(loanTable);
		loanedPane.add(imgPage);
	}

	static ArrayList<Book> checkedBook;
	private static void setupLoanPane(ArrayList<Book> basket, Book book) {
		loanPage = new JPanel();
		loanPage.setLayout(null);
		loanPage.add(new TitleLabel("대출"));
		try {
			img2 = ImageIO.read(new File("C:/Users/user/eclipse-workspace/JMT 팀 프로젝트/src/img/home.jpg"));
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "이미지 불러오기 실패");
		}
		ImageIcon icon = new ImageIcon(img2);
		JButton homeBtn = new JButton(icon);
		homeBtn.setBounds(50, 50, 70, 70);
		JButton loanBtn = new JButton("대출");
		loanBtn.setBounds(500, 350, 150, 50);

		TableSelectionDemo loanTable = new TableSelectionDemo();
		LoanMgr loan = LoanMgr.getInstance();
		if (basket != null) {
			checkedBook = basket;
			loan.setLoan(checkedBook);
		} else if (book != null) {
			checkedBook = new ArrayList<>();
			checkedBook.add(book);
			loan.setLoan(checkedBook);
		}
		loanTable.addComponentsToPane(loan);
		loanTable.setBounds(150, 150, 500, 150);

		// 시간 선택박스
		String[] time = { "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00" };
		JComboBox<String> timeCombo = new JComboBox<String>(time);
		// 날짜 선택박스
		String[] date = new String[7];
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		cal.setTime(new Date());
		for (int i = 0; i < 7; i++) {
			String tmp = dateFormat.format(new Date(cal.getTimeInMillis()));
			date[i] = tmp;
			cal.add(Calendar.DATE, 1);
		}
		JComboBox<String> dateCombo = new JComboBox<String>(date);
		dateCombo.setBounds(150, 350, 150, 50);
		timeCombo.setBounds(325, 350, 150, 50);

		cal.add(Calendar.DATE, lib.login_user.bookLoanDate[lib.login_user.position]);
		Date reDate = new Date(cal.getTimeInMillis());

		homeBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setupPage();
				frameRefresh(setPage);
			}
		});

		loanBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!lib.login_user.canLoan()) {
					JOptionPane.showMessageDialog(null, "대출 불가능 상태입니다.");
					return;
				}
				for (Book book : checkedBook) {
					lib.loanMgr.add(new BookLoan(book, lib.login_user, dateCombo.getSelectedItem().toString(),
							timeCombo.getSelectedItem().toString()));
				}
				String loanDate = dateCombo.getSelectedItem().toString();
				JOptionPane.showMessageDialog(null, "대출완료\n대출 권수: " + lib.login_user.loanNum() + "\n기간: " + loanDate
						+ "~" + dateFormat.format(reDate));
			}
		});

		loanPage.add(loanBtn);
		loanPage.add(dateCombo);
		loanPage.add(timeCombo);
		loanPage.add(homeBtn);
		loanPage.add(loanTable);
	}

	// TableSelectionDemo에서 refresh하려고 하니 오류나서 따로 만듦
	public static void toLoanPage(String s) {
		Book book = (Book) lib.bookMgr.find(s);
		setupLoanPane(null, book);
		frameRefresh(loanPage);
	}

	static void frameRefresh(JPanel nextPanel) {
		mainFrame.getContentPane().removeAll();
		mainFrame.getContentPane().add(nextPanel, BorderLayout.CENTER);
		mainFrame.revalidate();
		mainFrame.repaint();
	}
}

class menuBtn extends JButton {
	public menuBtn(String title) {
		setPreferredSize(new Dimension(200, 30));
		setText(title);
		setBackground(Color.lightGray);
		setContentAreaFilled(false);
		setBorderPainted(false);
		setFont(new Font("나눔고딕 ExtraBold", 0, 14));

		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				enteredColor();
			}

			@Override
			public void mouseExited(MouseEvent e) {
				exitedColor();
			}
		});
	}
	
	void enteredColor() {
		setContentAreaFilled(true);
	}

	void exitedColor() {
		setContentAreaFilled(false);
	}
}

class infoLabel extends JLabel {
	public infoLabel(String title) {
		setPreferredSize(new Dimension(200, 30));
		setText(title);
		setHorizontalAlignment(JLabel.CENTER);
		setFont(new Font("나눔고딕 ExtraBold", 0, 14));
	}
}

class TitleLabel extends JLabel {
	public TitleLabel(String title) {
		setText(title);
		setHorizontalAlignment(JLabel.CENTER);
		this.setFont(new Font("나눔고딕 ExtraBold", Font.BOLD, 11));
		setFont(this.getFont().deriveFont(40.0f));

		if (title.contentEquals("비대면 도서 대출"))
			setBounds(200, 90, 400, 100);
		else if (title.contentEquals("대출"))
			setBounds(200, 40, 400, 100);
		else
			setBounds(90, 45, 400, 100); // y좌표를 위로 10 올렸음
	}
}