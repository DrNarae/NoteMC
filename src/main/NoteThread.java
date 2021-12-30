package main;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class NoteThread extends Thread
{
	private Note NoteInfo = null;
	private ArrayList<String[]> Node = null;
	private Location locate = null;
	
	public NoteThread(Note n, Player p)
	{
		this.NoteInfo = n;
		this.Node = n.getNode();
		this.locate = new Location(p.getWorld(), p.getLocation().getX(), p.getLocation().getY(), p.getLocation().getZ());
	}
	
	public NoteThread(Note n, World world, double x, double y, double z)
	{
		this.NoteInfo = n;
		this.Node = n.getNode();
		this.locate = new Location(world, x, y, z);
	}
	
	public void run()
	{
		for (int i = 0; i < this.Node.size(); i++)
		{
			int syllable = 0;
			String beat = "";
			String inst = "";
			
			switch (this.Node.get(i)[0].toLowerCase())
			{
				case "도" :
				case "c" :
				{
					syllable = -6;
					break;
				}
				case "레" :
				case "d" :
				{
					syllable = -4;
					break;
				}
				case "미" :
				case "e" :
				{
					syllable = -2;
					break;
				}
				case "파" :
				case "f" :
				{
					syllable = -1;
					break;
				}
				case "솔" :
				case "g" :
				{
					syllable = -11;
					break;
				}
				case "라" :
				case "a" :
				{
					syllable = -9;
					break;
				}
				case "시" :
				case "b" :
				{
					syllable = -7;
					break;
				}
				case "r" :
				case "쉼" :
				{
					syllable = 999;
					break;
				}
				case "템포" :
				case "bpm" :
				{
					syllable = 998;
					break;
				}
				case "샵" :
				case "ds" :
				{
					syllable = 997;
					break;
				}
				case "플랫" :
				case "df" :
				{
					syllable = 996;
					break;
				}
				default :
				{
					syllable = 999;
					break;
				}
			}
			
			try
			{
				beat = this.Node.get(i)[2].equals("~") ? this.NoteInfo.getDefaultBeat() : this.Node.get(i)[2];
				int beatSec = 250;
				switch (beat)
				{
					case "1" :
					case "2" :
					case "4" :
					case "8" :
					case "16" :
					case "32" :
					case "64" :
					case "128" :
					case "256" :
					case "512" :
					{
						beat = (beatSec * (8 / Double.parseDouble(beat))) + "";
						break;
					}
					case "1." :
					case "2." :
					case "4." :
					case "8." :
					case "16." :
					case "32." :
					case "64." :
					case "128." :
					case "256." :
					case "512." :
					{
						beat = (beatSec * ((8 / Double.parseDouble(beat.replaceAll("\\.", ""))) + ((8 / Double.parseDouble(beat.replaceAll("\\.", ""))) / 2.0 )) ) + "";
						break;
					}
					default :
					{
						beat = "500";
						break;
					}
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
				System.out.println("노트 스레드 문제발생!");
				break;
			}

			// BPM 중간제어, 디폴트 플랫,샵 중간제어, 소리범위 제어 => (시간영향X)
			
			if (syllable < 996)
			{
				if (this.Node.get(i)[3].equals("#") && (syllable == -6 || syllable == -4 || syllable == -1 || syllable == -11 || syllable == -9))
				{
					syllable += 1;
				}
				else if (this.Node.get(i)[3].equals("$") && (syllable == -4 || syllable == -2 || syllable == -11 || syllable == -9 || syllable == -7))
				{
					syllable -= 1;
				}
				else if (this.Node.get(i)[3].equals("~"))
				{
					if (this.NoteInfo.getDefaultSharp().indexOf(syllable) >= 0)
					{
						syllable += 1;
					}
					else if (this.NoteInfo.getDefaultFlat().indexOf(syllable) >= 0)
					{
						syllable -= 1;
					}
				}
				
				inst = this.Node.get(i)[4].equals("~") ? this.NoteInfo.getDefaultInst() : this.NoteInfo.getDefineInst().get(this.Node.get(i)[4]); // hashmap!!!
				inst = (inst == null) ? this.NoteInfo.getDefaultInst() : inst;
				
				syllable += (this.Node.get(i)[1].equals("~") || this.Node.get(i)[1].equals("1")) ? 0 : 12;
				
				Bukkit.getWorld(this.locate.getWorld().getName()).playSound(this.locate, inst, 1, ( (float) Math.pow(2.0, (syllable/12.0) ) ));
				Bukkit.getWorld(this.locate.getWorld().getName()).spawnParticle(Particle.NOTE, this.locate, (int)((Math.random() * 3)+1));
				
				try
				{
					Thread.sleep((int)((Double.parseDouble(beat)) * (120 / ((double) this.NoteInfo.getDefaultBPM()))));
				}
				catch (Exception e)
				{
					e.printStackTrace();
					System.out.println("노트 스레드 문제발생!");
					break;
				}
			}
			else if (syllable == 996)
			{
				
			}
			else if (syllable == 997)
			{
				
			}
			else if (syllable == 998)
			{
				
			}
			
		}
	}
}
