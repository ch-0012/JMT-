package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.List;

import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import mgr.DataEngineInterface;
import mgr.UIdata;

import library.Book;
import library.Library;

public class TableSelectionDemo extends JPanel implements ListSelectionListener {
	private static final long serialVersionUID = 1L;
	DefaultTableModel tableModel;
	JTable table;
	int selectedIndex = -1;
	DataEngineInterface dataMgr;

	public TableSelectionDemo() {
		super(new BorderLayout());
	}

	void addComponentsToPane(DataEngineInterface mgr) {
		init(mgr);
		JScrollPane center = new JScrollPane(table);
		add(center, BorderLayout.CENTER);
	}
	
	void addComponentsToPane(DataEngineInterface mgr, String kwd) {
	      init2(mgr, kwd);
	      JScrollPane center = new JScrollPane(table);
	      add(center, BorderLayout.CENTER);
	   }
	
	   @SuppressWarnings("serial")
	   private void init2(DataEngineInterface mgr, String kwd) {
	      dataMgr = mgr;
	      tableModel = new DefaultTableModel(mgr.getColumnNames(), 0) {
	         public boolean isCellEditable(int row, int column) {
	            if (mgr.getType().contentEquals("BasketMgr") && column == 4)
	               return true;
	            return false;
	         }
	      };// �� ���� ���ϰ� �ϴ� �κ�
	      loadData(kwd);
	      table = new JTable(tableModel);
	      ListSelectionModel rowSM = table.getSelectionModel();
	      rowSM.addListSelectionListener(this);
	      table.setPreferredScrollableViewportSize(new Dimension(530, 300));
	      table.setFillsViewportHeight(true);
	      table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	      table.getTableHeader().setReorderingAllowed(false); // ��������
	      table.getTableHeader().setResizingAllowed(false);
	   }

	@SuppressWarnings("serial")
	void init(DataEngineInterface mgr) {
		dataMgr = mgr;
		tableModel = new DefaultTableModel(mgr.getColumnNames(), 0) { // �� ���� ���ϰ� �ϴ� �κ�
			public boolean isCellEditable(int row, int column) {
				if (mgr.getType().contentEquals("BasketMgr") && column == 4)
					return true;
				return false;
			}
		};
		loadData("");

		if (mgr.getType().contentEquals("BasketMgr")) {
			table = new JTable(tableModel) {
				@Override
				public Class getColumnClass(int column) {
					switch (column) {
					case 0:
						return String.class;
					case 1:
						return String.class;
					case 2:
						return String.class;
					case 3:
						return String.class;
					case 4:
						return Boolean.class;
					default:
						return String.class;
					}
				}
			};
		} else {
			table = new JTable(tableModel);
		}
		ListSelectionModel rowSM = table.getSelectionModel();
		rowSM.addListSelectionListener(this);
		table.setPreferredScrollableViewportSize(new Dimension(500, 300));
		table.setFillsViewportHeight(true);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.getTableHeader().setReorderingAllowed(false); // ��������
		table.getTableHeader().setResizingAllowed(false);
	}

	void setCheckBox() {
		JCheckBox checkBox = new JCheckBox();
		checkBox.setSelected(false);
		checkBox.setHorizontalAlignment(JLabel.CENTER);
		table.getColumn("����").setCellEditor(new DefaultCellEditor(checkBox));
		checkBox.setVisible(true);
		table.setPreferredScrollableViewportSize(new Dimension(500, 270));
	}

	void loadData(String kwd) {
		List<?> result = dataMgr.search(kwd);
		tableModel.setRowCount(0);
		for (Object m : result)
			tableModel.addRow(((UIdata) m).getUiTexts());
	}

	void showInfo() {
		if (selectedIndex < 0)
			return;
		String[] rowTexts = new String[tableModel.getColumnCount()];
		for (int i = 0; i < rowTexts.length; i++)
			rowTexts[i] = (String) tableModel.getValueAt(selectedIndex, i);
		DetailDialog dlg = new DetailDialog(rowTexts);
		dlg.setup();
		dlg.pack();
		dlg.setVisible(true);
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		ListSelectionModel lsm = (ListSelectionModel) e.getSource();
		if (!lsm.isSelectionEmpty()) {
			selectedIndex = lsm.getMinSelectionIndex();
		}
	}

	public void size(String title, int size) {
		table.getColumn(title).setPreferredWidth(size);
	}

	public class DetailDialog extends javax.swing.JDialog {
		String[] itemDetails;
		JLabel details[] = new JLabel[5];
		ImageIcon img = null;

		DetailDialog(String[] texts) {
			super(LibraryProgram.mainFrame);
			itemDetails = texts;
		}

		void setup() {
			setTitle("���� ����");
			JPanel pane = new JPanel(new BorderLayout());
			JPanel lpane = new JPanel(new GridLayout(4, 1));
			JPanel rpane = new JPanel(new GridLayout(2, 1));
			JButton basketBtn = new JButton("��ٱ��� ���");
			JButton loanBtn = new JButton("����");

			String isbn = ((Book) (Library.bookMgr.find(itemDetails[0]))).isbn;
			File imgFile = new File("C:/Users/user/eclipse-workspace/JMT �� ������Ʈ/src/img");
			String[] imgList = imgFile.list();
			String path = "";
			for (int i = 0; i < imgList.length; i++) {
				if (imgList[i].equals(isbn + ".jpg")) {
					path = "C:/Users/user/eclipse-workspace/JMT �� ������Ʈ/src/img/" + isbn + ".jpg";
					break;
				} else
					path = "C:/Users/user/eclipse-workspace/JMT �� ������Ʈ/src/img/null.jpg";
			}

			img = new ImageIcon(path);
			JPanel imgPane = new JPanel();
			JLabel image = new JLabel("", img, JLabel.CENTER);
			imgPane.add(image);

			details[0] = new JLabel("����: " + itemDetails[0]);
			details[1] = new JLabel("����: " + itemDetails[1]);
			details[2] = new JLabel("���ǻ�: " + itemDetails[2]);
			details[3] = new JLabel(">> " + itemDetails[3]);
			if (itemDetails[3].contentEquals("����Ұ���")) {
				basketBtn.setEnabled(false);
				loanBtn.setEnabled(false);
				details[3].setForeground(Color.RED);
			}
			lpane.add(details[0]);
			lpane.add(details[1]);
			lpane.add(details[2]);
			lpane.add(details[3]);

			basketBtn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					LibraryProgram.lib.basket.add(itemDetails[0]);
					JOptionPane.showMessageDialog(null, "��ٱ��� �ֱ� �Ϸ�");
				}
			});
			loanBtn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					LibraryProgram.toLoanPage(itemDetails[0]);
					setVisible(false);
				}
			});

			rpane.add(loanBtn);
			rpane.add(basketBtn);
			pane.add(imgPane, BorderLayout.WEST);
			pane.add(lpane, BorderLayout.CENTER);
			pane.add(rpane, BorderLayout.LINE_END);
			this.setMinimumSize(new Dimension(400, 150)); // ��ȭ���� ũ�� ����
			setLocationRelativeTo(null);
			setContentPane(pane);
		}
	}

}
