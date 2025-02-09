header { package fr.loria.ecoo.so6.antlr; 
import fr.loria.ecoo.so6.xml.node.*;
import fr.loria.ecoo.so6.xml.exception.*;
}


class XMLLexer extends Lexer ;

options {// some options
	k=3;
	charVocabulary='\u0000'..'\uFFFE';
}
{// lexer class members

}

// lexer rules

//XMLDECL_S				:	"<?xml"	;
//XMLDECL_E				:	"?>" ;

//VERSION					:	"version" ;
QUOTES					:	'\'' ;
DQUOTES					:	'"' ;

XML						:	(('X' | 'x') ('M' | 'm') ('L' | 'l')) ;

protected COMMENT_S		:	"<!--" ;
protected COMMENT_E		:	"-->" ;

COMMENT					: 	COMMENT_S! (options {greedy=false;}: . )* COMMENT_E! ;

//SPLIT			:	 "@#!SPLIT!#@" ; 
//						;

protected CDATA_S		:	"<![CDATA[" ;
protected CDATA_E		:	"]]>" ;
CDATA					: 	CDATA_S! (options {greedy=false;} : . )* CDATA_E!
						;

DOCTYPE_S				:	"<!DOCTYPE" ;
BOPEN					:	"[" ;
BCLOSE					:	"]" ;
DIESE					:	"#" ;
ESCAPE_LONGELEM_EE		:	"[[>" ;

PI_S			:	"<?" ;
PI_E			:	"?>" ;

//PI						:	PI_S! t:NAME (WS)? c:(options {greedy=false; } : . )* PI_E!
//						;

// textnode, element, attributes, doctype, ...

protected LETTER 		: BASECHAR | IDEOGRAPHIC 
						;
protected IDEOGRAPHIC 	: '\u4E00'..'\u9FA5' | '\u3007' | '\u3021'..'\u3029'
						;
protected BASECHAR		: '\u0041'..'\u005A' | '\u0061'..'\u007A' | '\u00C0'..'\u00D6' | '\u00D8'..'\u00F6' | '\u00F8'..'\u00FF' | '\u0100'..'\u0131' | '\u0134'..'\u013E' | '\u0141'..'\u0148' | '\u014A'..'\u017E' | '\u0180'..'\u01C3' | '\u01CD'..'\u01F0' | '\u01F4'..'\u01F5' | '\u01FA'..'\u0217' | '\u0250'..'\u02A8' | '\u02BB'..'\u02C1' | '\u0386' | '\u0388'..'\u038A' | '\u038C' | '\u038E'..'\u03A1' | '\u03A3'..'\u03CE' | '\u03D0'..'\u03D6' | '\u03DA' | '\u03DC' | '\u03DE' | '\u03E0' | '\u03E2'..'\u03F3' | '\u0401'..'\u040C' | '\u040E'..'\u044F' | '\u0451'..'\u045C' | '\u045E'..'\u0481' | '\u0490'..'\u04C4' | '\u04C7'..'\u04C8' | '\u04CB'..'\u04CC' | '\u04D0'..'\u04EB' | '\u04EE'..'\u04F5' | '\u04F8'..'\u04F9' | '\u0531'..'\u0556' | '\u0559' | '\u0561'..'\u0586' | '\u05D0'..'\u05EA' | '\u05F0'..'\u05F2' | '\u0621'..'\u063A' | '\u0641'..'\u064A' | '\u0671'..'\u06B7' | '\u06BA'..'\u06BE' | '\u06C0'..'\u06CE' | '\u06D0'..'\u06D3' | '\u06D5' | '\u06E5'..'\u06E6' | '\u0905'..'\u0939' | '\u093D' | '\u0958'..'\u0961' | '\u0985'..'\u098C' | '\u098F'..'\u0990' | '\u0993'..'\u09A8' | '\u09AA'..'\u09B0' | '\u09B2' | '\u09B6'..'\u09B9' | '\u09DC'..'\u09DD' | '\u09DF'..'\u09E1' | '\u09F0'..'\u09F1' | '\u0A05'..'\u0A0A' | '\u0A0F'..'\u0A10' | '\u0A13'..'\u0A28' | '\u0A2A'..'\u0A30' | '\u0A32'..'\u0A33' | '\u0A35'..'\u0A36' | '\u0A38'..'\u0A39' | '\u0A59'..'\u0A5C' | '\u0A5E' | '\u0A72'..'\u0A74' | '\u0A85'..'\u0A8B' | '\u0A8D' | '\u0A8F'..'\u0A91' | '\u0A93'..'\u0AA8' | '\u0AAA'..'\u0AB0' | '\u0AB2'..'\u0AB3' | '\u0AB5'..'\u0AB9' | '\u0ABD' | '\u0AE0' | '\u0B05'..'\u0B0C' | '\u0B0F'..'\u0B10' | '\u0B13'..'\u0B28' | '\u0B2A'..'\u0B30' | '\u0B32'..'\u0B33' | '\u0B36'..'\u0B39' | '\u0B3D' | '\u0B5C'..'\u0B5D' | '\u0B5F'..'\u0B61' | '\u0B85'..'\u0B8A' | '\u0B8E'..'\u0B90' | '\u0B92'..'\u0B95' | '\u0B99'..'\u0B9A' | '\u0B9C' | '\u0B9E'..'\u0B9F' | '\u0BA3'..'\u0BA4' | '\u0BA8'..'\u0BAA' | '\u0BAE'..'\u0BB5' | '\u0BB7'..'\u0BB9' | '\u0C05'..'\u0C0C' | '\u0C0E'..'\u0C10' | '\u0C12'..'\u0C28' | '\u0C2A'..'\u0C33' | '\u0C35'..'\u0C39' | '\u0C60'..'\u0C61' | '\u0C85'..'\u0C8C' | '\u0C8E'..'\u0C90' | '\u0C92'..'\u0CA8' | '\u0CAA'..'\u0CB3' | '\u0CB5'..'\u0CB9' | '\u0CDE' | '\u0CE0'..'\u0CE1' | '\u0D05'..'\u0D0C' | '\u0D0E'..'\u0D10' | '\u0D12'..'\u0D28' | '\u0D2A'..'\u0D39' | '\u0D60'..'\u0D61' | '\u0E01'..'\u0E2E' | '\u0E30' | '\u0E32'..'\u0E33' | '\u0E40'..'\u0E45' | '\u0E81'..'\u0E82' | '\u0E84' | '\u0E87'..'\u0E88' | '\u0E8A' | '\u0E8D' | '\u0E94'..'\u0E97' | '\u0E99'..'\u0E9F' | '\u0EA1'..'\u0EA3' | '\u0EA5' | '\u0EA7' | '\u0EAA'..'\u0EAB' | '\u0EAD'..'\u0EAE' | '\u0EB0' | '\u0EB2'..'\u0EB3' | '\u0EBD' | '\u0EC0'..'\u0EC4' | '\u0F40'..'\u0F47' | '\u0F49'..'\u0F69' | '\u10A0'..'\u10C5' | '\u10D0'..'\u10F6' | '\u1100' | '\u1102'..'\u1103' | '\u1105'..'\u1107' | '\u1109' | '\u110B'..'\u110C' | '\u110E'..'\u1112' | '\u113C' | '\u113E' | '\u1140' | '\u114C' | '\u114E' | '\u1150' | '\u1154'..'\u1155' | '\u1159' | '\u115F'..'\u1161' | '\u1163' | '\u1165' | '\u1167' | '\u1169' | '\u116D'..'\u116E' | '\u1172'..'\u1173' | '\u1175' | '\u119E' | '\u11A8' | '\u11AB' | '\u11AE'..'\u11AF' | '\u11B7'..'\u11B8' | '\u11BA' | '\u11BC'..'\u11C2' | '\u11EB' | '\u11F0' | '\u11F9' | '\u1E00'..'\u1E9B' | '\u1EA0'..'\u1EF9' | '\u1F00'..'\u1F15' | '\u1F18'..'\u1F1D' | '\u1F20'..'\u1F45' | '\u1F48'..'\u1F4D' | '\u1F50'..'\u1F57' | '\u1F59' | '\u1F5B' | '\u1F5D' | '\u1F5F'..'\u1F7D' | '\u1F80'..'\u1FB4' | '\u1FB6'..'\u1FBC' | '\u1FBE' | '\u1FC2'..'\u1FC4' | '\u1FC6'..'\u1FCC' | '\u1FD0'..'\u1FD3' | '\u1FD6'..'\u1FDB' | '\u1FE0'..'\u1FEC' | '\u1FF2'..'\u1FF4' | '\u1FF6'..'\u1FFC' | '\u2126' | '\u212A'..'\u212B' | '\u212E' | '\u2180'..'\u2182' | '\u3041'..'\u3094' | '\u30A1'..'\u30FA' | '\u3105'..'\u312C' | '\uAC00'..'\uD7A3'
						;
protected DIGIT			: '\u0030'..'\u0039' | '\u0660'..'\u0669' | '\u06F0'..'\u06F9' | '\u0966'..'\u096F' | '\u09E6'..'\u09EF' | '\u0A66'..'\u0A6F' | '\u0AE6'..'\u0AEF' | '\u0B66'..'\u0B6F' | '\u0BE7'..'\u0BEF' | '\u0C66'..'\u0C6F' | '\u0CE6'..'\u0CEF' | '\u0D66'..'\u0D6F' | '\u0E50'..'\u0E59' | '\u0ED0'..'\u0ED9' | '\u0F20'..'\u0F29'
						;
protected COMBININGCHAR	: '\u0300'..'\u0345' | '\u0360'..'\u0361' | '\u0483'..'\u0486' | '\u0591'..'\u05A1' | '\u05A3'..'\u05B9' | '\u05BB'..'\u05BD' | '\u05BF' | '\u05C1'..'\u05C2' | '\u05C4' | '\u064B'..'\u0652' | '\u0670' | '\u06D6'..'\u06DC' | '\u06DD'..'\u06DF' | '\u06E0'..'\u06E4' | '\u06E7'..'\u06E8' | '\u06EA'..'\u06ED' | '\u0901'..'\u0903' | '\u093C' | '\u093E'..'\u094C' | '\u094D' | '\u0951'..'\u0954' | '\u0962'..'\u0963' | '\u0981'..'\u0983' | '\u09BC' | '\u09BE' | '\u09BF' | '\u09C0'..'\u09C4' | '\u09C7'..'\u09C8' | '\u09CB'..'\u09CD' | '\u09D7' | '\u09E2'..'\u09E3' | '\u0A02' | '\u0A3C' | '\u0A3E' | '\u0A3F' | '\u0A40'..'\u0A42' | '\u0A47'..'\u0A48' | '\u0A4B'..'\u0A4D' | '\u0A70'..'\u0A71' | '\u0A81'..'\u0A83' | '\u0ABC' | '\u0ABE'..'\u0AC5' | '\u0AC7'..'\u0AC9' | '\u0ACB'..'\u0ACD' | '\u0B01'..'\u0B03' | '\u0B3C' | '\u0B3E'..'\u0B43' | '\u0B47'..'\u0B48' | '\u0B4B'..'\u0B4D' | '\u0B56'..'\u0B57' | '\u0B82'..'\u0B83' | '\u0BBE'..'\u0BC2' | '\u0BC6'..'\u0BC8' | '\u0BCA'..'\u0BCD' | '\u0BD7' | '\u0C01'..'\u0C03' | '\u0C3E'..'\u0C44' | '\u0C46'..'\u0C48' | '\u0C4A'..'\u0C4D' | '\u0C55'..'\u0C56' | '\u0C82'..'\u0C83' | '\u0CBE'..'\u0CC4' | '\u0CC6'..'\u0CC8' | '\u0CCA'..'\u0CCD' | '\u0CD5'..'\u0CD6' | '\u0D02'..'\u0D03' | '\u0D3E'..'\u0D43' | '\u0D46'..'\u0D48' | '\u0D4A'..'\u0D4D' | '\u0D57' | '\u0E31' | '\u0E34'..'\u0E3A' | '\u0E47'..'\u0E4E' | '\u0EB1' | '\u0EB4'..'\u0EB9' | '\u0EBB'..'\u0EBC' | '\u0EC8'..'\u0ECD' | '\u0F18'..'\u0F19' | '\u0F35' | '\u0F37' | '\u0F39' | '\u0F3E' | '\u0F3F' | '\u0F71'..'\u0F84' | '\u0F86'..'\u0F8B' | '\u0F90'..'\u0F95' | '\u0F97' | '\u0F99'..'\u0FAD' | '\u0FB1'..'\u0FB7' | '\u0FB9' | '\u20D0'..'\u20DC' | '\u20E1' | '\u302A'..'\u302F' | '\u3099' | '\u309A'
						;
protected EXTENDER		: '\u00B7' | '\u02D0' | '\u02D1' | '\u0387' | '\u0640' | '\u0E46' | '\u0EC6' | '\u3005' | '\u3031'..'\u3035' | '\u309D'..'\u309E' | '\u30FC'..'\u30FE'
						;
protected NAMECHAR 		: LETTER | DIGIT | '.' | '-' | '_' | ':' | COMBININGCHAR | EXTENDER
						;

//NAME_START    	:	('a'..'z' | 'A'..'Z' | '_' | ':' | '-' | '0'..'9'  )+    

NAME_START    	:	(LETTER | '_' | ':')+                      
				;
NAME_SUITE		:	(NAMECHAR)+ //('.')+
                ;

//ATT_VAL					:	DQUOTES! (options {greedy=false;} : . )* DQUOTES!
//						|	QUOTES! (options {greedy=false;} : . )* QUOTES!
//						;

ELEM_S			:	"<" ;
SHORTELEM_E		:	"/>" ;
LONGELEM_ES		:	"</" ;
LONGELEM_EE		:	">" ;
EQ				:	"=" ;
WS				:	('\n' | '\r' | '\t' | ' ')+ ;

TEXT			:	(~('a'..'z' | 'A'..'Z' | '_' | ':' | '-' | '0'..'9' | '<' | '>' |'"' | '\'' | '[' | ']' ))+
				;

 

{
import java.util.ArrayList;
}
class XMLParser extends Parser ;
options{
	defaultErrorHandler = false;
}
{
public static void joinSiblingTextNodes(TreeNode node) {
		if (node.hasChildren()) {
			int numChildren = node.getChildren().size();
			TreeNode aux;
			ArrayList<TreeNode> removedNodes = new ArrayList<>();
			int startPos = -1;
			StringBuilder sb = new StringBuilder();
			
			for (int i=0; i < numChildren; i++) {
				aux = node.getChild(i);
				
				if (aux instanceof TextNode && !(aux instanceof CommentNode)) {
					if (startPos == -1) {
						startPos = i;
					} else {
						sb.append(((TextNode)aux).getContent());
						removedNodes.add(aux);
					}
				}
				else {
					if (startPos != -1 && sb.length() != 0) {
						((TextNode)node.getChild(startPos)).appendContent(sb.toString());
						sb = new StringBuilder();
					}
					
					startPos = -1;

					if (aux instanceof ElementNode) {
						joinSiblingTextNodes(aux);
					}
				}
			}
			
			if (startPos != -1 && sb.length() != 0) {
				((TextNode)node.getChild(startPos)).appendContent(sb.toString());
			}
			for (TreeNode removedNode : removedNodes) {
				node.removeChild(removedNode);
			}
		}
	}
}

document returns [Document doc=new Document();] throws Exception
{ TreeNode n=null,e=null;String vers=null;}
								:	
									(prolog[doc])?
									(
									 n=node { if (n!=null) { doc.appendChild(n); } } 
									)* EOF
									{ joinSiblingTextNodes(doc); }
								;

node returns [TreeNode n=null;] throws ParseException, AttributeAlreadyExist
									: 	n=elem
									|	n=misc
									;

prolog[Document d] throws Exception //String vers=null;]	
{ String[] anAtt = null; }			
								:	//XMLDECL_S WS 
									PI_S XML WS
									(anAtt=attribute
									{ 
										if ("version".equals(anAtt[0])) {
											d.setVersion(anAtt[1]);	
										} else if ("encoding".equals(anAtt[0])) {
											d.setEncoding(anAtt[1]);	
										} else if("standalone".equals(anAtt[0])){
											d.setStandalone(anAtt[1]);
										} else {
											throw new Exception("unsupported attribute in xml declaration:"+anAtt[0]+"="+anAtt[1]);
										}

									} (WS)?)+
									PI_E
								;

name_suite returns [String name=null]
{ StringBuilder buf = new StringBuilder();}
								: (x:XML  
								    { buf.append(x.getText()); }
								  |  ns:NAME_SUITE 
								    { buf.append(ns.getText()); }
								  | xs:NAME_START 
								    { buf.append(xs.getText()); }
								  )+ 
								    { name = buf.toString(); }
								;

name returns [String name=null]
{ StringBuilder buf=new StringBuilder();
  String n=null;
}
								:
								( x:XML 
								     { buf.append(x.getText()); }
								  ( 
								    n=name_suite()
									 { buf.append(n); }
								  )?
								| s:NAME_START 
								      { buf.append(s.getText()); }
								  ( 
								    n=name_suite()  
								      { buf.append(n); }
								  )?
								)  
								   { name = buf.toString(); }
								;

name_processing returns [String name=null]
{ StringBuilder buf = new StringBuilder();
  String ss = null;
 }
								: ( s:NAME_START 
								      {buf.append(s.getText()); }
								    ( 
								      ss=name_suite()  { buf.append(ss); }
								    )?								    
								  | x:XML ss=name_suite()
									{
									  buf.append(x.getText());
									  buf.append(ss);
									}
							      ) { name = buf.toString(); }
								;

attribute returns [String[] result = new String[2]]			
{ String v=null; String n = null;}
//								:	n:NAME (WS)? EQ (WS)? v:ATT_VAL  
								:	n=name() (WS)? EQ (WS)? v=attributeSuite
									{ 
										result[0]=n;
										result[1]=v;
									}
								;

attributeSuite returns [String result = null]
{ String x,y;}
								:  DQUOTES x=attributeValueWithoutDQuotes DQUOTES
									{
										result = x;	
									}
								|  QUOTES y=attributeValueWithoutQuotes QUOTES
									{
										result = y;	
									}
								;
								
commonAttributeValue returns [String result = null]
{ StringBuilder buf = new StringBuilder(); String n = null;}
								: (t:TEXT 
								     { buf.append(t.getText()); }
								  | n=name_suite() 
								     { buf.append(n); }
								  | x:XML 
								     { buf.append(x.getText()); }
								  | ws:WS
								  	{ buf.append(ws.getText());  } 
								  | diese:DIESE
								  	{ buf.append(diese.getText()); }
								  | lee:LONGELEM_EE
								  	{ buf.append(lee.getText()); }
								  | bopen:BOPEN
								  	{ buf.append(bopen.getText());}
								  | bclose:BCLOSE
								  	{ buf.append(bclose.getText());}
								  | se_e:SHORTELEM_E
								  	{
								  		buf.append(se_e.getText());
								  	}
								  | pi_e:PI_E
								  	{
								  		buf.append(pi_e.getText());
								  	}  
								  | eq:EQ
								  	{
								  		buf.append(eq.getText());
								  	}
								  )
								  { result = buf.toString(); }
								;
								
attributeValueWithoutQuotes returns [String result = null]
{ StringBuilder buf = new StringBuilder(); 
	String cav = null;}
								: (	cav=commonAttributeValue()
									{ buf.append(cav);}
								  | dq:DQUOTES
								     { buf.append(dq.getText()); }
								  )*
								     { result = buf.toString(); }
								;

attributeValueWithoutDQuotes returns [String result = null]
{ StringBuilder buf = new StringBuilder(); 
	String cav = null;}
								: (	cav=commonAttributeValue()
									{ buf.append(cav);}
								  | q:QUOTES
								     { buf.append(q.getText()); }
								  )*
								     { result = buf.toString(); }
								;

								
								

misc returns [TreeNode n=null;]
{ TextNode t=null;
  String x = null;
  String y = null;	
}	
								:	c:COMMENT 
									{
										if (! "SPLIT".equals(c.getText())) {
											n=new CommentNode(c.getText());
										}
									} 	
								| 	n = processingInstruction()					
									
									
								|   d:CDATA
									{
										n=new CDataNode(d.getText());	
									}
								|   DOCTYPE_S                        	{ t=new DocTypeNode(); }				                   
								    	(text_wo_bracket[t]
								    |	q:QUOTES						{t.appendContent(q.getText());}
								    |	dq:DQUOTES						{t.appendContent(dq.getText());} 
								    )+
								    ( BOPEN                          	{ t.appendContent("["); }
								      (   text_wo_bracket[t]
								      	| string[t]
								      	| ELEM_S						{t.appendContent("<");}
								      	| LONGELEM_EE					{t.appendContent(">");}
								      	| LONGELEM_ES					{t.appendContent("</");}
								      	| SHORTELEM_E					{t.appendContent("/>");}
										| comment:COMMENT				{t.appendContent("<!--");t.appendContent(comment.getText());t.appendContent("-->");}
								        | pi_s:PI_S						{t.appendContent("<?");}
								        | pi_e:PI_E						{t.appendContent("?>");}
								        | cdata:CDATA					{t.appendContent("<!CDATA[");t.appendContent(cdata.getText());t.appendContent("]]>");}																	
								      )+								      
								      BCLOSE                          { t.appendContent("]"); }
								    )?   
								    ( s:WS							  { t.appendContent(s.getText()); }
								    )?                           
								    LONGELEM_EE                       { n = t; }
//								|	SPLIT
								|	{
										t=new TextNode();
									}
									text[t]
									{
										n = t;
									}									
								;

string[TextNode n]
{}
								: ( dq:DQUOTES  					{ n.appendContent(dq.getText()); }
								    (string_wo_dquotes[n])*
								    DQUOTES 						{ n.appendContent(dq.getText()); }
								  | q:QUOTES 						{ n.appendContent(q.getText()); }
								  	(string_wo_quotes[n])*
								  	QUOTES 							{ n.appendContent(q.getText()); }
								  )+ 
								;
								
string_wo_dquotes[TextNode n]
{}
								:	text_wo_bracket[n]
								|	bopen:BOPEN
												{	
													n.appendContent(bopen.getText());
												}						
												
								|	bclose:BCLOSE
												{	
													n.appendContent(bclose.getText());
												}	
								| elt_s:ELEM_S 
												{
													n.appendContent(elt_s.getText());
												}
								| les:LONGELEM_ES
												{
													n.appendContent(les.getText());
												}
						  		| lee:LONGELEM_EE
												{
													n.appendContent(lee.getText());
												}	
								| se_e:SHORTELEM_E	
												{
													n.appendContent(se_e.getText());
												}													
								|	q:QUOTES
												{	
													n.appendContent(q.getText());
												}
								| c:COMMENT
												{
													n.appendContent("<!--");
													n.appendContent(c.getText());	
													n.appendContent("-->");
												}
								| cd:CDATA
												{
													n.appendContent("<![CDATA[");
													n.appendContent(cd.getText());
													n.appendContent("]]>");
												}
								| pi_s:PI_S		{n.appendContent("<?");}
								| pi_e:PI_E		{n.appendContent("?>");}																			
								;

string_wo_quotes[TextNode n]
{}
								:	text_wo_bracket[n]
								|	bopen:BOPEN
												{	
													n.appendContent(bopen.getText());
												}						
												
								|	bclose:BCLOSE
												{	
													n.appendContent(bclose.getText());
												}	
								| elt_s:ELEM_S 
												{
													n.appendContent(elt_s.getText());
												}
								| les:LONGELEM_ES
												{
													n.appendContent(les.getText());
												}												
						  		| lee:LONGELEM_EE
												{
													n.appendContent(lee.getText());
												}	
								| se_e:SHORTELEM_E	
												{
													n.appendContent(se_e.getText());
												}													
								| dq:DQUOTES
												{	
													n.appendContent(dq.getText());
												}								
								| c:COMMENT
												{
													n.appendContent("<!--");
													n.appendContent(c.getText());	
													n.appendContent("-->");
												}
								| cd:CDATA
												{
													n.appendContent("<![CDATA[");
													n.appendContent(cd.getText());
													n.appendContent("]]>");
												}
								| pi_s:PI_S		{n.appendContent("<?");}
								| pi_e:PI_E		{n.appendContent("?>");}
													
								;
			
processingInstruction returns [ProcessingInstructionNode pi = null;]
{String name = null;}
								:	PI_S
										{
											pi = new ProcessingInstructionNode();
										}
									name=name_processing()
										{
											pi.setTarget(name);
										}
									piContent[pi]
									PI_E	
								;
								
piContent[ProcessingInstructionNode pi]
{}
								:	(w:WS
										{pi.appendContent(w.getText());} 
									(
										t:TEXT
										{pi.appendContent(t.getText());}
									|	ns:NAME_START
										{pi.appendContent(ns.getText());}
									|	n:NAME_SUITE
										{pi.appendContent(n.getText());}
									|	dq:DQUOTES
										{pi.appendContent(dq.getText());}
									|	q:QUOTES
										{pi.appendContent(q.getText());}
									|	bo:BOPEN
										{pi.appendContent(bo.getText());}
									|	bc:BCLOSE
										{pi.appendContent(bc.getText());}
									|	e_s:ELEM_S
										{pi.appendContent(e_s.getText());}
									|	le_ee:LONGELEM_EE
										{pi.appendContent(le_ee.getText());}
									|	le_es:LONGELEM_ES
										{pi.appendContent(le_es.getText());}
									|	dt_s:DOCTYPE_S
										{pi.appendContent(dt_s.getText());}
									|	pi_s:PI_S
										{pi.appendContent(pi_s.getText());}
									|	ws:WS
										{pi.appendContent(ws.getText());}
									|	eq:EQ
										{pi.appendContent(eq.getText());}
									)*)?
								;

text_wo_bracket[TextNode n]
{String na = null;}
								:(   t:TEXT
												{
													n.appendContent(t.getText());		
												}
									   
//									 text_suite[n]

								|	ns:NAME_SUITE {
									                n.appendContent(ns.getText());
												}

								|	na=name()
												{
													n.appendContent(na);										
												}
//									 text_suite[n]
								|  s:WS
												{
													n.appendContent(s.getText());	
												}
//									 text_suite[n]
								|	eq:EQ
												{	
													n.appendContent(eq.getText());
												}												
								|	diese:DIESE
												{
													n.appendContent(diese.getText());
												}
								| ele:ESCAPE_LONGELEM_EE
												{
													n.appendContent(ele.getText());
												}
								| q:QUOTE { n.appendContent(q.getText()); }
								| dq:DQUOTE { n.appendContent(dq.getText()); }
								)+
								;										
												
								
									
text[TextNode n]
{}
								:	(text_wo_bracket[n]
								|	bopen:BOPEN
												{	
													n.appendContent(bopen.getText());
												}						
												
								|	bclose:BCLOSE
												{	
													n.appendContent(bclose.getText());
												}	
						  		| lee:LONGELEM_EE
												{
													n.appendContent(lee.getText());
												}	
								| se_e:SHORTELEM_E	
												{
													n.appendContent(se_e.getText());
												}	
												
								|	dq:DQUOTES
												{	
													n.appendContent(dq.getText());
												}												
								|	q:QUOTES
												{	
													n.appendContent(q.getText());
												}	
												)+							
								;

//text_suite[TextNode n]
//								: (text[n])?
//								;

elem returns [ElementNode elt=null;] throws ParseException, AttributeAlreadyExist
{String name = null;}
								:	ELEM_S name=name() 
											{
												elt = new ElementNode(name);
											} 
									esuite[elt] 
								;
								
esuite[ElementNode elt] throws ParseException, AttributeAlreadyExist
								:	SHORTELEM_E
								|	LONGELEM_EE long_suite[elt]
								|	WS esuite2[elt] 
								;

esuite2[ElementNode elt] throws ParseException, AttributeAlreadyExist
{ 	String[] anAtt = null;
	String s; 
}		
								:	SHORTELEM_E
								|	LONGELEM_EE long_suite[elt]
								|   anAtt = attribute
											{
												s = (String)elt.getAttribute(anAtt[0]);
												if(s!=null) {
													throw new AttributeAlreadyExist(anAtt[0]);
												}
												elt.setAttribute(anAtt[0], anAtt[1]);
											}
									(WS)? esuite2[elt]
								;

long_suite[ElementNode elt] throws ParseException, AttributeAlreadyExist
{	TreeNode t=null;
	ElementNode e=null;
	String s = null;
}
								: 
								  (t=node {if (t!=null) { elt.appendChild(t); }}
								  )* LONGELEM_ES s=name() {if (!elt.getElementName().equals(s)) { throw new ParseException("The tag name must be equal"); }} (WS)? LONGELEM_EE
								;

								