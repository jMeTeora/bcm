package bcm;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;

public final class Chi {
	static String chars = " абвгдеёжзийклмнопрстуфхцчшщъыьэюя";
	static String digs = "0123456789";

	public static void main(String[] args) throws IOException {
		boolean read = true;// false for write
		File in;
		File out;

		if (args.length != 3) {
			System.err.println("3 args:\n\n");
			System.err.println("1 arg:true for decode");
			System.err.println("1 arg:false for encode");

			System.err.println("2 inputFile");
			System.err.println("3 outputFile");
			Runtime.getRuntime().exit(1);
		}

		read = Boolean.valueOf(args[0]);
		in = new File(args[1]);
		out = new File(args[2]);
		if (!out.exists()) {
			try {
				if (!out.getParentFile().exists()) {
					out.getParentFile().mkdirs();
				}
			} catch (Exception e) {
				// IGNORE||HIDE
			}
			Files.createFile(out.toPath());
		}

		try (FileInputStream fis = new FileInputStream(in); FileOutputStream fos = new FileOutputStream(out)) {
			byte[] readed = fis.readAllBytes();
			if (read) {
				// read->dec->write
				String decoded = dec(readed);
				fos.write(decoded.getBytes(StandardCharsets.UTF_8));
			} else {
				// read->enc->write
				String readedString = new String(readed, StandardCharsets.UTF_8);
				ArrayList<Byte> encrypted = inc(readedString);

				byte[] fw = new byte[encrypted.size()];
				for (int i = 0; i < encrypted.size(); i++) {
					fw[i] = encrypted.get(i);
				}
				fos.write(fw);
				fos.flush();
			}
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	private static String dec(byte[] readed) {
		StringBuilder answerBuilder = new StringBuilder();
		for (int i = 0; i < readed.length; i++) {
			byte b = readed[i];
			int index = b;
			if (index >= 100) {
				index = index - 100;
				char cha = digs.charAt(index);
				answerBuilder.append(cha);
			} else if (index < 0) {
				System.err.println("Chi.dec(index < 0):" + index);
				Runtime.getRuntime().exit(2);
			} else {
				try {
					char cha = chars.charAt(index);
					answerBuilder.append(cha);
				} catch (Exception e) {
					e.printStackTrace();
					System.err.println("Chi.dec(ERROR):" + index);
					Runtime.getRuntime().exit(3);
				}
			}
		}
		return answerBuilder.toString();
	}

	private static ArrayList<Byte> inc(String string) {
		return inc(string.toLowerCase().toCharArray());
	}

	private static ArrayList<Byte> inc(char[] charArray) {
		ArrayList<Byte> answr = new ArrayList<Byte>();
		for (int i = 0; i < charArray.length; i++) {
			try {
				char c = charArray[i];
				ArrayList<Byte> aaa = Chi.getByteForChar(c);
				answr.addAll(aaa);
			} catch (Exception e) {
				System.err.println(e.getMessage());
			}
		}
		return answr;
	}

	private static ArrayList<Byte> getByteForChar(char c) {
		ArrayList<Byte> answ = new ArrayList<Byte>();

		int eee = c;
		if (eee == 10) {
			return answ;
		}

		int index1 = 0;
		for (Character character : chars.toCharArray()) {
			if (character.equals(c)) {
				answ.add((byte) index1);
				return answ;
			}
			index1++;
		}

		Character chaaaa = c;
		int index2 = 100;
		for (Character character : digs.toCharArray()) {
			if (character == chaaaa) {
				answ.add((byte) index2);
				return answ;
			}
			index2++;
		}
		throw new UnsupportedOperationException(String.format("pls impl for [%s] int:[%s]", c, eee));
	}

}
