package myTest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;

public class FileSystemDemo0923 {

	public static void main(String[] args) {
		ViewClass viewClass = new ViewClass();
		// viewClass.toDemo();
		viewClass.mainMenu();

	}

}

/**
 * ������ʾ��
 * 
 * @author silladus
 *
 */
class ViewClass {
	private Scanner input;

	private FileOpt fileOpt;
	// ��¼�û������·��
	private String curruFilePath;

	// public void toDemo() {
	public ViewClass() {
		input = new Scanner(System.in);
		fileOpt = new FileOpt();
	}

	public void mainMenu() {
		System.out.println("==========�ļ�����==========");
		System.out.println("1.�����ļ�");
		System.out.println("2.ɾ���ļ�");
		System.out.println("3.�����ļ�");
		System.out.println("4.�����ļ���");
		System.out.println("5.�鿴�ļ���");
		System.out.println("6.�����ļ�");
		System.out.println("7.�����ض��ļ���");
		System.out.println("��ѡ��");
		int opt = input.nextInt();
		switch (opt) {
		case 1:
			// �����ļ�
			copyFile();
			mainMenu();
			break;
		case 2:
			// ɾ���ļ�
			System.out.println("����Ҫɾ�����ļ�");
			String Path = input.next();
			File file = new File(Path);
			fileDele(file);
			mainMenu();
			break;
		case 3:
			// �����ļ�
			createFile();
			mainMenu();
			break;
		case 4:
			// �����ļ���
			nMdir();
			mainMenu();
			break;
		case 5:
			// �鿴�ļ���
			showFile();
			mainMenu();
			break;
		case 6:
			// �����ļ�
			moveFile();
			mainMenu();
			break;
		case 7:
			// �����ض��ļ���
			System.out.println("��������·��");
			String searchPath = input.next();
			System.out.println("�����ļ�����");
			String keyStr = input.next();
			File searchResults = new File(searchPath);
			fileDele(searchResults, keyStr);
			mainMenu();
			break;
		}
	}

	// ��ʾ�ļ�
	public void showFile() {
		System.out.println("������Ҫ�鿴��·��");
		String path = input.next();
		curruFilePath = path;
		File[] fs = fileOpt.showFileChilden(path);
		if (fs != null) {
			System.out
					.println("--------------" + fs.length + "���ļ�------------");
			for (File file : fs) {
				String wString = "�ļ�";
				if (file.isDirectory()) {
					wString = "�ļ���";
				}
				System.out.println(file.getName() + "\t" + wString);
			}

			System.out.println();
			// ��ʾ�ļ������˵�
			mainMenu();
		} else {
			System.out.println("��·��������");
			System.out.println();
			showFile();
		}
	}

	// �����ļ�
	public void copyFile() {
		System.out.println("������Ҫ���Ƶ��ļ�");
		String fName = input.next();
		System.out.println("������Ҫ���Ƶ�·��");
		String targetPath = input.next();
		String source = curruFilePath + "\\" + fName;
		String target = targetPath + "\\" + fName;
		boolean r = fileOpt.copy(source, target);
		if (r) {
			System.out.println("�������");
		} else {
			System.out.println("����ʧ��");
		}
	}

	// ɾ���ļ�
	public void fileDele(File file) {
		if (file.isFile()) {
			boolean fd = file.delete();
			if (fd) {
				System.out.println(file.getPath() + " ɾ�����");
			}
		} else {
			// ����ļ������ݲ�Ϊ����ִ��1������ֱ��ִ��2
			// 1
			File[] fs = file.listFiles();
			for (File f : fs) {
				if (f.isFile()) {
					boolean fd2 = f.delete();
					if (fd2) {
						System.out.println(f.getPath() + " ɾ�����");
					}
				} else {
					fileDele(f);
				}
			}
			// 2
			boolean md = file.delete();
			if (md) {
				System.out.println(file.getPath() + "\t�ļ���ɾ�����");
			}
		}
	}

	// ɾ���ض��ļ���
	public void fileDele(File file, String name) {
		File[] fs = file.listFiles();
		for (File f : fs) {
			if (f.getName().equals(name)) {
				fileDele(f);
			} else {
				if (f.isDirectory()) {
					fileDele(f, name);
				}
			}
		}
	}

	// �����ļ�
	public void createFile() {
		System.out.println("�������ļ���");
		String path = input.next();
		if (path.contains(".")) {
			// �����ļ�
			boolean isCreate = fileOpt.createFile(path);
			if (isCreate) {
				System.out.println("�ļ��������");
			} else {
				System.err.println("�ļ�����ʧ��");
			}
		} else {
			System.err.println("��������չ��");
		}
	}

	// �����ļ���
	public void nMdir() {
		System.out.println("�������ļ���·��");
		String path = input.next();
		boolean isDir = fileOpt.nMdirs(path);
		if (isDir) {
			System.out.println("�ļ��д������");
		} else {
			System.err.println("�ļ��д���ʧ��");
		}
	}

	// �����ļ�
	public void moveFile() {
		System.out.println("��ѡ��Ҫ�ƶ����ļ�");
		String fromString = input.next();
		System.out.println("�ƶ�����");
		String toString = input.next();
		boolean isMove = fileOpt.moveFile(fromString, toString);
		if (isMove) {
			System.out.println("�ļ��ƶ����");
		} else {
			System.out.println("�ļ��ƶ�ʧ��");
		}
	}
}

/**
 * �ļ�������
 * 
 * @author silladus
 *
 */
class FileOpt {
	// �����ļ�
	public boolean copy(String source, String target) {
		try {
			FileInputStream in = new FileInputStream(source);
			FileOutputStream out = new FileOutputStream(target);
			int content = 0;
			while ((content = in.read()) != -1) {
				out.write(content);
			}
			out.flush();
			out.close();
			in.close();
			return true;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	// ��ʾ�ļ�
	public File[] showFileChilden(String path) {
		File f = new File(path);
		if (f != null && f.isDirectory()) {
			File[] fs = f.listFiles();
			return fs;
		}
		return null;
	}

	// �����ļ�
	public boolean createFile(String path) {
		File nFile = new File(path);
		// �����ļ�
		boolean isCreate = false;
		try {
			isCreate = nFile.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (isCreate) {
			// System.out.println(nFile.getName() + " �������");
			return true;
		} else {
			// System.out.println("�ļ�����ʧ��");
			return false;
		}
	}

	// �����ļ���
	public boolean nMdirs(String path) {
		// �����ļ���
		File nDir = new File(path);
		boolean isDir = nDir.mkdirs();
		if (isDir) {
			// System.out.println(nDir.getName() + " �������");
			return true;
		} else {
			// System.out.println("�ļ��д���ʧ��");
			return false;
		}
	}

	// �����ļ�
	public boolean moveFile(String from, String to) {
		File sFile = new File(from);
		File tFile = new File(to);
		boolean isRename = sFile.renameTo(tFile);
		if (isRename) {
			return true;
		} else {
			return false;
		}
	}
}