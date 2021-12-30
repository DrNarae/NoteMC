package main;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.ChatColor;

public class Main extends JavaPlugin
{
	public void onEnable()
	{
		createConfig();
		System.out.println("[노트크래프트] 작곡을 도와주는 마법사, 노트크래프트 입니다.");
		System.out.println("[노트크래프트] 관리자님 안녕하세요?");
	}
	
	public void onDisable()
	{
		System.out.println("[노트크래프트] 작곡을 도와주는 마법사, 노트크래프트 였습니다.");
		System.out.println("[노트크래프트] 관리자님 안녕히가십시오.");
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if (!cmd.getName().equalsIgnoreCase("notemc")) return false;
		
		if (args.length == 0)
		{
			String message = "";
			message += ChatColor.LIGHT_PURPLE + "[] = 선택, <> = 필수\n";
			message += ChatColor.AQUA + "/notemc" + ChatColor.GREEN + " create" + ChatColor.RED + " <name>\n";
			message += ChatColor.AQUA + "/notemc" + ChatColor.GREEN + " delete" + ChatColor.RED + " <name>\n";
			message += ChatColor.AQUA + "/notemc" + ChatColor.GREEN + " play" + ChatColor.RED + " <PlayerName> <NoteName1> [NoteName2] . . .\n";
			message += ChatColor.AQUA + "/notemc" + ChatColor.GREEN + " locationplay" + ChatColor.RED + " <WorldName> <X> <Y> <Z> <NoteName1> [NoteName2] . . .\n";
			message += ChatColor.AQUA + "/notemc" + ChatColor.GREEN + " stop\n";
			message += ChatColor.AQUA + "/notemc" + ChatColor.GREEN + " list\n";
			message += ChatColor.AQUA + "/notemc" + ChatColor.GREEN + " help" + ChatColor.RED + " <create | delete | list>\n";
			sender.sendMessage(message);
			
			return false;
		}
		if (args[0].equalsIgnoreCase("create"))
		{
			createNote((args.length >= 2 ? args[1]:""), sender);
			return true;
		}
		else if (args[0].equalsIgnoreCase("delete") && args.length == 2)
		{
			deleteNote(args[1]);
			return true;
		}
		else if (args[0].equalsIgnoreCase("play") && args.length >= 3)
		{
			playNote(args, sender, true);
			return true;
		}
		else if (args[0].equalsIgnoreCase("locationplay") && args.length >= 6)
		{
			playNote(args, sender, false);
			return true;
		}
		else if (args[0].equalsIgnoreCase("stop") && sender instanceof Player)
		{

			return true;
		}
		else if (args[0].equalsIgnoreCase("list") && sender instanceof Player)
		{
			showList(sender);
			return true;
		}
		else if (args[0].equalsIgnoreCase("help") && sender instanceof Player && args.length == 2)
		{
			helpInformation(args[1], sender);
			return true;
		}
		else
		{
			String message = "";
			message += ChatColor.LIGHT_PURPLE + "[] = 선택, <> = 필수\n";
			message += ChatColor.AQUA + "/notemc" + ChatColor.GREEN + " create" + ChatColor.RED + " [name]\n";
			message += ChatColor.AQUA + "/notemc" + ChatColor.GREEN + " delete" + ChatColor.RED + " <name>\n";
			message += ChatColor.AQUA + "/notemc" + ChatColor.GREEN + " play" + ChatColor.RED + " <PlayerName> <NoteName1> [NoteName2] . . .\n";
			message += ChatColor.AQUA + "/notemc" + ChatColor.GREEN + " locationplay" + ChatColor.RED + " <WorldName> <X> <Y> <Z> <NoteName1> [NoteName2] . . .\n";
			message += ChatColor.AQUA + "/notemc" + ChatColor.GREEN + " stop\n";
			message += ChatColor.AQUA + "/notemc" + ChatColor.GREEN + " list\n";
			message += ChatColor.AQUA + "/notemc" + ChatColor.GREEN + " help" + ChatColor.RED + " <create | delete | play | stop | list>\n";
			sender.sendMessage(message);
			
			return false;
		}
	}
	
	public void createNote(String fileName, CommandSender sender)
	{
		String dir = "";
		File newFile = null;
		BufferedOutputStream bs = null;
		String form = "";
		form += "description:설명\n";
		form += "defaultBeat=1,2,4,8,16,32,64,128 중 1개 입력\n";
		form += "defaultInst=기본악기이름\n";
		form += "defaultBPM=1~200중 숫자 1개 입력\n";
		form += "defaultSharp=도,레,파,솔,라 중 기본으로 샵 처리 할 계이름 여러 개 입력\n";
		form += "defaultFlat=레,미,솔,라,시 중 기본으로 플랫 처리 할 계이름 여러 개 입력\n";
		form += "instDefine=악기1-악기이름,악기2-악기이름,악기3-악기이름, . . .\n";
		form += "note=악보입력";
		
		try
		{
			if (fileName.equalsIgnoreCase(""))
			{
				int count = 2;
				dir = "plugins/NoteMC/Note/newNote.txt";
				newFile = new File(dir);
				
				while (!newFile.createNewFile())
				{
					dir = "plugins/NoteMC/Note/newNote(" + count + ").txt";
					newFile = new File(dir);
					count++;
				}
				
				bs = new BufferedOutputStream(new FileOutputStream(dir));
				bs.write(form.getBytes());
				bs.close();
				sender.sendMessage(ChatColor.GOLD + "새 작곡파일을 생성하였습니다. - newNote" + (count >= 3 ? "(" + (count-1) + ")":"") + ".txt");
			}
			else
			{
				dir = "plugins/NoteMC/Note/" + fileName + ".txt";
				newFile = new File(dir);
				
				if (newFile.createNewFile())
				{
					bs = new BufferedOutputStream(new FileOutputStream(dir));
					
					bs.write(form.getBytes());
					bs.close();
					sender.sendMessage(ChatColor.GOLD + "새 작곡파일을 생성하였습니다. - " + fileName + ".txt");
				}
				else
				{
					sender.sendMessage(ChatColor.RED + "새 작곡파일생성에 실패하였습니다. - " + fileName + ".txt");
				}
			}
		}
		catch (IOException e)
		{
			sender.sendMessage(ChatColor.RED + "새 작곡파일생성에 실패하였습니다.");
		}
	}
	
	public void deleteNote(String fileName)
	{
		
	}
	
	public void playNote(String[] name, CommandSender sender, Boolean b)
	{
		// => [계이름, 옥타브넘버, 박자, 내츄럴, 악기]
		// 계이름 : 도, c, 레, d
		// 옥타브넘버 : 솔~파 = 1, 솔~파 = 2
		// 박자 : 1,2,4,8,16,32,64,128,256,512 또는 ~
		// 내츄럴 : # => 샵, $ => 플랫, @외 아무거나 => 내츄럴, ~ => 자동
		// 악기 : 악보에서 정의한 악기기호
		Player p = null;
		World w = null;
		double x = 0.0;
		double y = 0.0;
		double z = 0.0;
		int indexStart = 0;
		
		if (b)
		{
			p = Bukkit.getPlayer(name[1]);
			if (p == null)
			{
				sender.sendMessage(ChatColor.RED + "해당 플레이어를 찾을 수 없습니다.");
				return;
			}
			else if (!p.isOnline())
			{
				sender.sendMessage(ChatColor.GRAY + "해당 플레이어는 오프라인입니다.");
				return;
			}
			indexStart = 2;
		}
		else
		{
			try
			{
				w = Bukkit.getWorld(name[1]);
				if (w == null)
				{
					sender.sendMessage(ChatColor.RED + "존재하지 않는 월드입니다.");
					return;
				}
				x = Double.parseDouble(name[2]);
				y = Double.parseDouble(name[3]);
				z = Double.parseDouble(name[4]);
				indexStart = 5;
			}
			catch(Exception e)
			{
				sender.sendMessage(ChatColor.RED + "좌표설정이 잘못되었습니다.");
				return;
			}
		}
		
		ArrayList<String> noteText = new ArrayList<String>();
		String dir = "";
		for (int i = indexStart; i < name.length; i++)
		{
			dir = "plugins/NoteMC/Note/" + name[i] + ".txt";
			FileInputStream fileStream = null;
			try
			{
				fileStream = new FileInputStream(dir);
				byte[] readBuffer = new byte[fileStream.available()];
				while (fileStream.read(readBuffer) != -1) {}
				noteText.add(new String(readBuffer));
				fileStream.close();
			}
			catch (IOException e)
			{
				sender.sendMessage(ChatColor.RED + name[i] + "의 작곡파일에서 문제가 생겼습니다.");
				return;
			} 
		}
		
		ArrayList<Note> Notes = new ArrayList<Note>();
		for (int i = 0; i < noteText.size(); i++)
		{
			Note newNote = decodingNote(noteText.get(i));
			if (newNote != null)
			{
				Notes.add(newNote);
			}
			else
			{
				sender.sendMessage(ChatColor.RED + name[i+indexStart] + "의 작곡파일에서 해독 중 문제가 생겼습니다.");
				return;
			}
		}
		
		// 쓰레드 ㄱㄱㄱ
		if (b)
		{
			for (int i = 0; i < Notes.size(); i++)
			{
				NoteThread t = new NoteThread(Notes.get(i), p);
				t.start();
			}
		}
		else
		{
			for (int i = 0; i < Notes.size(); i++)
			{
				NoteThread t = new NoteThread(Notes.get(i), w, x, y, z);
				t.start();
			}
		}
	}
	
	public Note decodingNote(String NoteStr)
	{
		try
		{
			HashMap<String, String> instInfo = new HashMap<String, String>();
			String[] list = NoteStr.split("note=")[0].split("\n");
			
			String b = list[1].split("=")[1];
			String in = list[2].split("=")[1];
			int bpm = Integer.parseInt(list[3].split("=")[1]);
			ArrayList<Integer> s = list[4].split("=").length == 1 ? new ArrayList<Integer>() : decodingSign(list[4].split("=")[1].split(","), true);
			ArrayList<Integer> f = list[5].split("=").length == 1 ? new ArrayList<Integer>() : decodingSign(list[5].split("=")[1].split(","), false);

			String[] instTemp = list[6].replace("instDefine=", "").equalsIgnoreCase("") ? new String[0] : list[6].replace("instDefine=", "").split(",");
			for (int i = 0; i < instTemp.length; i++)
			{
				instInfo.put(instTemp[i].split("-")[0], instTemp[i].split("-")[1]);
			}
			
			ArrayList<String[]> node = decodingNode(NoteStr.split("note=")[1].replaceAll("\n", " ").split(" "));
			if (node != null)
			{
				return new Note(b, in, bpm, s, f, instInfo, node);
			}
			else
			{
				return null;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	public ArrayList<Integer> decodingSign(String[] signlist, Boolean sf)
	{
		ArrayList<Integer> codelist = new ArrayList<Integer>();
		
		for (int i = 0; i < signlist.length; i++)
		{
			if (sf)
			{
				switch (signlist[i])
				{
					case "도" :
					{
						codelist.add(-6);
						break;
					}
					case "레" :
					{
						codelist.add(-4);
						break;
					}
					case "파" :
					{
						codelist.add(-1);
						break;
					}
					case "솔" :
					{
						codelist.add(-11);
						break;
					}
					case "라" :
					{
						codelist.add(-9);
						break;
					}
					case "c" :
					{
						codelist.add(-6);
						break;
					}
					case "d" :
					{
						codelist.add(-4);
						break;
					}
					case "f" :
					{
						codelist.add(-1);
						break;
					}
					case "g" :
					{
						codelist.add(-11);
						break;
					}
					case "a" :
					{
						codelist.add(-9);
						break;
					}
				}
			}
			else
			{
				switch (signlist[i])
				{
					case "레" :
					{
						codelist.add(-4);
						break;
					}
					case "미" :
					{
						codelist.add(-2);
						break;
					}
					case "솔" :
					{
						codelist.add(-11);
						break;
					}
					case "라" :
					{
						codelist.add(-9);
						break;
					}
					case "시" :
					{
						codelist.add(-7);
						break;
					}
					case "d" :
					{
						codelist.add(-4);
						break;
					}
					case "e" :
					{
						codelist.add(-2);
						break;
					}
					case "g" :
					{
						codelist.add(-11);
						break;
					}
					case "a" :
					{
						codelist.add(-9);
						break;
					}
					case "b" :
					{
						codelist.add(-7);
						break;
					}
				}
			}
		}
		
		return codelist;
	}
	
	public ArrayList<String[]> decodingNode(String[] nodes)
	{
		ArrayList<String[]> decodedNodes = new ArrayList<String[]>();
		
		for (int i = 0; i < nodes.length; i++)
		{
			String[] tmp = nodes[i].split(",");
			
			switch (tmp.length)
			{
				case 1 :
				{
					String[] node = new String[5];
					node[0] = tmp[0];
					node[1] = "~";
					node[2] = "~";
					node[3] = "~";
					node[4] = "~";
					decodedNodes.add(node);
					break;
				}
				case 2 :
				{
					String[] node = new String[5];
					node[0] = tmp[0];
					node[1] = tmp[1];
					node[2] = "~";
					node[3] = "~";
					node[4] = "~";
					decodedNodes.add(node);
					break;
				}
				case 3 :
				{
					String[] node = new String[5];
					node[0] = tmp[0];
					node[1] = tmp[1];
					node[2] = tmp[2];
					node[3] = "~";
					node[4] = "~";
					decodedNodes.add(node);
					break;
				}
				case 4 :
				{
					String[] node = new String[5];
					node[0] = tmp[0];
					node[1] = tmp[1];
					node[2] = tmp[2];
					node[3] = tmp[3];
					node[4] = "~";
					decodedNodes.add(node);
					break;
				}
				case 5 :
				{
					String[] node = new String[5];
					node[0] = tmp[0];
					node[1] = tmp[1];
					node[2] = tmp[2];
					node[3] = tmp[3];
					node[4] = tmp[4];
					decodedNodes.add(node);
					break;
				}
				default :
				{
					return null;
				}
			}
		}
		
		return decodedNodes;
	}
	
	public void stopNote()
	{
		
	}
	
	public void showList(CommandSender sender)
	{
		File fileDir = new File("plugins/NoteMC/Note");
		String[] List = fileDir.list();
		
		if (List.length > 0)
		{
			String listS = "";
			listS += ChatColor.BLACK + "----------------------\n";
			for (int i = 0; i < List.length; i++)
			{
				listS += ChatColor.GREEN + List[i] + "\n";
			}
			listS += ChatColor.BLACK + "----------------------";
			sender.sendMessage(listS);
		}
		else
		{
			sender.sendMessage(ChatColor.RED + "악보가 존재하지 않습니다.");
		}
	}
	
	public void helpInformation(String key, CommandSender sender)
	{
		String message = "";
		
		switch (key)
		{
			case "create":
			{
				message += ChatColor.AQUA + "작곡파일을 생성합니다.\n";
				message += "작곡파일 양식 (대소문자 구분)\n";
				message += ChatColor.BLACK + "------------------------------\n";
				message += ChatColor.LIGHT_PURPLE + "description:설명\n";
				message += "defaultBeat=1,2,4,8,16,32,64,128 중 1개 입력\n";
				message += "defaultInst=기본악기이름\n";
				message += "defaultBPM=1~200중 숫자 1개 입력\n";
				message += "defaultSharp=도,레,파,솔,라 중 기본으로 샵 처리 할 계이름 여러 개 입력\n";
				message += "defaultFlat=레,미,솔,라,시 중 기본으로 플랫 처리 할 계이름 여러 개 입력\n";
				message += "instDefine=악기1-악기이름,악기2-악기이름,악기3-악기이름, . . .\n";
				message += "note=악보\n";
				message += ChatColor.BLACK + "------------------------------\n";
				message += ChatColor.GREEN + "파일 확장자:txt\n";
				message += "자세한 설명 : \n";
				break;
			}
			case "delete":
			{
				message += ChatColor.AQUA + "작곡파일을 삭제합니다. (복원 불가)";
				break;
			}
			case "play":
			{
				break;
			}
			case "stop":
			{
				break;
			}
			case "list":
			{
				message += ChatColor.AQUA + "작곡파일의 목록을 보여줍니다.";
				break;
			}
			default :
			{
				message += ChatColor.RED + "없는 명령입니다.";
				break;
			}
		}
		sender.sendMessage(message);
	}
	
	public void createConfig()
	{
		File fileDir = new File("plugins/NoteMC/config.yml");
		FileConfiguration config = YamlConfiguration.loadConfiguration(fileDir);
		if (!fileDir.exists())
		{
			config.options().copyDefaults(true);
			config.addDefault("test1", "-1");
			try
			{
				config.save(fileDir);
				(new File("plugins/NoteMC/Note")).mkdir();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
}
